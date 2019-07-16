/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author wangwendi
 * @version $Id: ThreadPoolTaskExecutorUtil.java, v 0.1 2019年07月16日 下午8:33 wangwendi Exp $
 */
public class ThreadPoolTaskExecutorUtil {

    private static final ThreadPoolTaskExecutorUtil instance = new ThreadPoolTaskExecutorUtil();

    /**
     * 线程池
     */
    private volatile ThreadPoolTaskExecutor poolTaskExecutor;

    public static ThreadPoolTaskExecutorUtil newInstance(){
        return instance;
    }

    public void setPoolTaskExecutor(ThreadPoolTaskExecutor poolTaskExecutor) {
        this.poolTaskExecutor = poolTaskExecutor;
    }

    public ThreadPoolTaskExecutor getPoolTaskExecutor() {
        return poolTaskExecutor;
    }



}