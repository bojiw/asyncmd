/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.model;

import com.asyncmd.config.AsynGroupConfig;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.enums.DispatchMode;
import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.manager.AsynExecuterFacade;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.utils.CountException;
import com.asyncmd.utils.FrequencyUtil;
import com.asyncmd.utils.ThreadPoolTaskExecutorUtil;
import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 异步命令执行器
 *
 * @author wangwendi
 * @version $Id: AbstractAsynExecuter.java, v 0.1 2018年09月20日 wangwendi Exp $
 */
public abstract class AbstractAsynExecuter<T extends AsynCmd> implements InitializingBean {

    private  Log log = LogFactory.getLog(this.getClass());


    /**
     * 调度模式 默认为异步调度
     */
    protected DispatchMode dispatchMode = DispatchMode.ASYN;

    protected static ThreadLocal<AsynException> localException = new ThreadLocal<AsynException>();

    /**
     * 是否自动注册
     */
    protected boolean autoRegister = true;

    @Autowired
    private AsynExecuterFacade asynExecuterFacade;

    @Autowired
    private AsynExecuterService asynExecuterService;

    /**
     * 如果有特别的异步命令不想要用全局重试频率 可以设置
     * 如果调度模式为异步或者同步调度 则第一次无论设置多少都是立即执行
     * 重试执行频率 5s,10s,1m,1h
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次1分钟以后执行 第四次1小时以后执行 之后都是间隔1小时执行
     * 执行频率 5s,10s,20s
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次20秒以后执行
     */
    protected String executerFrequencys;

    private List<Frequency> executerFrequencyList = new ArrayList<Frequency>();

    @Autowired
    private AsynGroupConfig asynGroupConfig;


    /**
     * 异步执行 使用CountDownLatch实现伪同步
     * @param asynCmd
     */
    private void pseudoAsy(T asynCmd){

        CountException countException = new CountException();
        poolAsynExecuter(asynCmd,countException);
        //如果360秒以后异步执行还是没有完 则直接结束等待
        boolean await = countException.await(360);
        //如果返回的是false代表不是正常执行结束 把异常放入进去
        if (!await){
            log.error("同步执行异常");
            localException.set(new AsynException(AsynExCode.SYS_ERROR));
        }
        //把异常放入到本地线程中 方便其他地方获取
        localException.set(countException.getException());


    }

    /**
     * 同步执行
     * @param asynCmd
     * @param isTransaction 是否在事务的回调中
     */
    public void asyExecuter(AsynCmd asynCmd,boolean isTransaction){
        T t = (T)asynCmd;
        if (isTransaction){
            //如果业务代码存在事务 在事务回调中再存在事务会有问题 所以使用伪同步来处理
            pseudoAsy(t);
        }else {
            CountException countException = new CountException();
            //执行run方法是同步执行的
            new AsynRunnable(t,countException).run();
            //把异常放入到本地线程中 方便其他地方获取
            localException.set(countException.getException());
        }

    }

    /**
     * 异步执行
     */
    public boolean asynExecuter(AsynCmd asynCmd){
        T t = (T)asynCmd;
        return poolAsynExecuter(t,null);
    }

    /**
     * 线程池中执行异步命令
     * @param asynCmd
     * @param countException
     */
    public boolean poolAsynExecuter(T asynCmd,CountException countException){
        try {
            ThreadPoolTaskExecutorUtil.newInstance().getPoolTaskExecutor().execute(new AsynRunnable(asynCmd,countException));
        }catch (Exception e){
            log.warn("asyn线程池异常",e);
            return false;
        }
        return true;

    }


    /**
     * 执行线程
     */
    class AsynRunnable implements Runnable{

        private T asynCmd;
        private CountException countException;

        public  AsynRunnable(T asynCmd,CountException countException){
            this.asynCmd = asynCmd;
            this.countException = countException;
        }

        public void run() {
            try {
                executer(asynCmd);
                asynExecuterService.backupCmd(asynCmd);
            }catch (Exception e){
                //失败更新状态为初始化 由调度中心调度
                AsynUpdateParam asynUpdateParam = new AsynUpdateParam();
                if (asynCmd.getExecuteNum() >= asynGroupConfig.getAsynConfig().getRetryNum()){
                    asynUpdateParam.setStatus(AsynStatus.ERROR.getStatus());

                }else {
                    asynUpdateParam.setStatus(AsynStatus.INIT.getStatus());

                    asynUpdateParam.setNextTime(asynExecuterService.getNextTime(executerFrequencyList,asynCmd));

                }
                asynUpdateParam.setBizId(asynCmd.getBizId());
                asynUpdateParam.setWhereAsynStatus(AsynStatus.EXECUTE.getStatus());

                asynExecuterService.updateStatus(asynUpdateParam);
                if (countNotNull()){
                    countException.setException(new AsynException(AsynExCode.SYS_ERROR,e));
                }
            }finally {
                if (countNotNull()){
                    countException.countDown();
                }
            }
        }



        private boolean countNotNull(){
            return countException != null;
        }
    }


    /**
     * 由子类执行具体逻辑
     * @param cmd
     */
    protected void executer(T cmd){};


    /**
     * 初始化的时候注册到执行器容器中
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.isEmpty(executerFrequencys)){
            executerFrequencyList = FrequencyUtil.createFrequencys(executerFrequencys);
        }
        //如果是自动注册 直接在初始化的时候注册
        if (autoRegister){
            asynExecuterFacade.registerAsynExecuter(this);
        }
    }

    public void setAutoRegister(boolean autoRegister) {
        this.autoRegister = autoRegister;
    }

    public void setAsynExecuterFacade(AsynExecuterFacade asynExecuterFacade) {
        this.asynExecuterFacade = asynExecuterFacade;
    }

    public void setExecuterFrequencys(String executerFrequencys) {
        this.executerFrequencys = executerFrequencys;
    }

    public void setDispatchMode(DispatchMode dispatchMode) {
        this.dispatchMode = dispatchMode;
    }

    public DispatchMode getDispatchMode() {
        return dispatchMode;
    }

    public List<Frequency> getExecuterFrequencyList() {
        return executerFrequencyList;
    }
}

