/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.utils;

import com.asyncmd.exception.AsynException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 * 线程异常管理类
 * @author wangwendi
 * @version $Id: CountException.java, v 0.1 2018年09月21日 下午5:29 wangwendi Exp $
 */
public class CountException {

    private static Log            log = LogFactory.getLog(CountException.class);
    /**
     * 线程控制工具
     */
    private        CountDownLatch countDownLatch;
    /**
     * 执行异常
     */
    private        AsynException  exception;

    public CountException(){
        this.countDownLatch = new CountDownLatch(1);
    }

    /**
     * 暂停线程 单位为秒
     * @param time
     * @return 如果不是正常执行结束则返回fasle
     */
    public boolean await(long time){
        try {
            return countDownLatch.await(time, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error("CountException等待异步线程执行异常");
            return false;
        }
    }

    public void countDown(){
        countDownLatch.countDown();
    }

    public AsynException getException() {
        return exception;
    }

    public void setException(AsynException exception) {
        this.exception = exception;
    }



}