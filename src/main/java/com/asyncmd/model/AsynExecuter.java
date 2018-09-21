/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.model;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
     * 同步执行
     *
     */
    public void asyExecuter(AsynCmd asynCmd){

    }

    /**
     * 异步执行
     */
    public void asynExecuter(AsynCmd asynCmd){
        poolTaskExecutor.execute(new AsynRunnable(asynCmd));
    }


    class AsynRunnable implements Runnable{
        AsynRunnable(AsynCmd asynCmd){

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

