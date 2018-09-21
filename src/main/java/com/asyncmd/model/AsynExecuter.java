/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.model;

import com.asyncmd.enums.DispatchMode;
import com.asyncmd.utils.CountException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 * 异步命令执行器
 * @author wangwendi
 * @version $Id: AsynExecuter.java, v 0.1 2018年09月20日 下午8:13 wangwendi Exp $
 */
public abstract class AsynExecuter implements InitializingBean {

    /**
     * 线程池
     */
    private ThreadPoolTaskExecutor poolTaskExecutor;

    /**
     * 调度模式 默认由调度中心进行调度
     */
    private DispatchMode dispatchMode = DispatchMode.DEFAULT;

    /**
     * 异步执行 使用CountDownLatch实现伪同步
     * @param asynCmd
     */
    private void pseudoAsy(AsynCmd asynCmd){

        try {
            CountException countException = new CountException();

            poolAsynExecuter(asynCmd,countException);
            //如果120秒以后异步执行还是没有完 则直接结束等待
            countException.await(120);
        }catch (Exception e){

        }


    }

    /**
     * 同步执行
     * @param asynCmd
     * @param isTransaction 是否在事务的回调中
     */
    public void asyExecuter(AsynCmd asynCmd,boolean isTransaction){
        if (isTransaction){
            //如果业务代码存在事务 在事务回调中再存在事务会有问题 所以使用伪同步来处理


        }else {
            new AsynRunnable(asynCmd,null).run();
        }

    }

    /**
     * 异步执行
     */
    public void asynExecuter(AsynCmd asynCmd){
        poolAsynExecuter(asynCmd,null);
    }

    /**
     * 线程池中执行异步命令
     * @param asynCmd
     * @param countException
     */
    private void poolAsynExecuter(AsynCmd asynCmd,CountException countException){
        poolTaskExecutor.execute(new AsynRunnable(asynCmd,countException));

    }


    /**
     * 执行线程
     */
    class AsynRunnable implements Runnable{

        private AsynCmd asynCmd;
        private CountException countException;

        public  AsynRunnable(AsynCmd asynCmd,CountException countException){
            this.asynCmd = asynCmd;
            this.countException = countException;
        }

        public void run() {
        }
    }


    /**
     * 由子类执行具体逻辑
     * @param cmd
     */
    protected void executer(AsynCmd cmd){};


    /**
     * 初始化的时候注册到执行器容器中
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {

    }




}

