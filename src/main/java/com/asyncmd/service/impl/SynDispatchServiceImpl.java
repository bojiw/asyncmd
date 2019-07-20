/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.service.impl;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynRunnable;
import com.asyncmd.utils.CountException;
import com.asyncmd.utils.LocalExceptionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

/**
 * 同步执行
 * @author wangwendi
 * @version $Id: SynDispatchServiceImpl.java, v 0.1 2019年07月19日 下午11:53 wangwendi Exp $
 */
public class SynDispatchServiceImpl extends AbstractDispatchService{
    private Log log = LogFactory.getLog(this.getClass());

    public void dispatch(AsynCmd asynCmd, List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList) {
        asyExecuter(asynExecuterList,asynCmd);
    }


    /**
     * 异步执行 使用CountDownLatch实现伪同步
     * @param asynCmd
     */
    private void pseudoAsy(AsynCmd asynCmd,List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList){

        CountException countException = new CountException();
        poolAsynExecuter(asynCmd,asynExecuterList,countException);
        //如果360秒以后异步执行还是没有完 则直接结束等待
        boolean await = countException.await(360);
        //如果返回的是false代表不是正常执行结束 把异常放入进去
        if (!await){
            log.error("同步执行异常");
            LocalExceptionUtil.set(new AsynException(AsynExCode.SYS_ERROR));
        }
        //把异常放入到本地线程中 方便其他地方获取
        LocalExceptionUtil.set(countException.getException());


    }

    /**
     * 同步执行
     * @param asynExecuterList
     * @param asynCmd
     */
    private void asyExecuter(List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList, AsynCmd asynCmd){
        if (TransactionSynchronizationManager.isActualTransactionActive()){
            //如果在事务内 则在事务的回调中执行 防止执行器中也有事务导致嵌套事务
            TransactionSynchronizationManager
                    .registerSynchronization(new SynDispatchServiceImpl.AsyTransactionSynchronization(asynCmd,asynExecuterList));

        }else {
            asyExecuter(asynCmd,asynExecuterList,false);
        }

    }

    /**
     * 同步执行
     * @param asynCmd
     * @param isTransaction 是否在事务的回调中
     */
    final public void asyExecuter(AsynCmd asynCmd,List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList,boolean isTransaction){
        if (isTransaction){
            //如果业务代码存在事务 在事务回调中再存在事务会有问题 所以使用伪同步来处理
            pseudoAsy(asynCmd,asynExecuterList);
        }else {
            CountException countException = new CountException();
            //执行run方法是同步执行的
            AsynRunnable asynRunnable = createAsynRunnable(asynCmd, asynExecuterList);
            asynRunnable.countException(countException);
            asynRunnable.run();
            //把异常放入到本地线程中 方便其他地方获取
            LocalExceptionUtil.set(countException.getException());
        }

    }

    class AsyTransactionSynchronization implements TransactionSynchronization {

        private AsynCmd              asynCmd;
        private List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList;

        AsyTransactionSynchronization(AsynCmd asynCmd,List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList){
            this.asynCmd = asynCmd;
            this.asynExecuterList = asynExecuterList;
        }

        @Override
        public void afterCompletion(int i) {
            //如果事务已经提交 则同步执行命令
            if (TransactionSynchronization.STATUS_COMMITTED == i){
                asyExecuter(asynCmd,asynExecuterList,true);
            }
        }
        @Override
        public void suspend() {

        }
        @Override
        public void resume() {

        }
        @Override
        public void flush() {

        }
        @Override
        public void beforeCommit(boolean b) {

        }
        @Override
        public void beforeCompletion() {

        }
        @Override
        public void afterCommit() {

        }
    }
}