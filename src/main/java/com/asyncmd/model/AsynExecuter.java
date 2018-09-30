/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.model;

import com.asyncmd.enums.DispatchMode;
import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.utils.CountException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步命令执行器
 *
 * @author wangwendi
 * @version $Id: AsynExecuter.java, v 0.1 2018年09月20日 wangwendi Exp $
 */
public abstract class AsynExecuter<T extends AsynCmd> implements InitializingBean {

    private  Log log = LogFactory.getLog(this.getClass());

    /**
     * 线程池
     */
    private ThreadPoolTaskExecutor poolTaskExecutor;

    /**
     * 调度模式 默认由调度中心进行调度
     */
    private DispatchMode dispatchMode = DispatchMode.DEFAULT;

    private ThreadLocal<AsynException> localException = new ThreadLocal<AsynException>();

    /**
     * 异步执行 使用CountDownLatch实现伪同步
     * @param asynCmd
     */
    private void pseudoAsy(T asynCmd){

        CountException countException = new CountException();
        poolAsynExecuter(asynCmd,countException);
        //如果120秒以后异步执行还是没有完 则直接结束等待
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
    public void asyExecuter(T asynCmd,boolean isTransaction){
        if (isTransaction){
            //如果业务代码存在事务 在事务回调中再存在事务会有问题 所以使用伪同步来处理
            pseudoAsy(asynCmd);
        }else {
            CountException countException = new CountException();
            //执行run方法是同步执行的
            new AsynRunnable(asynCmd,countException).run();
            //把异常放入到本地线程中 方便其他地方获取
            localException.set(countException.getException());
        }

    }

    /**
     * 异步执行
     */
    public void asynExecuter(T asynCmd){
        poolAsynExecuter(asynCmd,null);
    }

    /**
     * 线程池中执行异步命令
     * @param asynCmd
     * @param countException
     */
    private void poolAsynExecuter(T asynCmd,CountException countException){
        poolTaskExecutor.execute(new AsynRunnable(asynCmd,countException));

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
            }catch (Exception e){
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

    }




}

