/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.utils;

import com.asyncmd.exception.AsynException;

/**
 * @author wangwendi
 * @version $Id: LocalExceptionUtil.java, v 0.1 2019年07月20日 下午1:51 wangwendi Exp $
 */
public class LocalExceptionUtil {
    protected static ThreadLocal<AsynException> localException = new ThreadLocal<AsynException>();


    public static void set(AsynException asynException){
        localException.set(asynException);
    }


    public static AsynException get() {
        localException.get();
        return localException.get();
    }
}