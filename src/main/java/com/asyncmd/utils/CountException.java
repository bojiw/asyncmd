/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author wangwendi
 * @version $Id: CountException.java, v 0.1 2018年09月21日 下午5:29 wangwendi Exp $
 */
public class CountException {
    /**
     * 线程控制工具
     */
    private CountDownLatch countDownLatch;
    /**
     * 执行异常
     */
    private Exception exception;

    public CountException(){
        this.countDownLatch = new CountDownLatch(1);
    }

    /**
     * 暂停线程 单位为秒
     * @param time
     */
    public void await(long time){
        try {
            countDownLatch.await(time,TimeUnit.SECONDS);
        }catch (Exception e){

        }
    }

    public void countDown(){
        countDownLatch.countDown();
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}