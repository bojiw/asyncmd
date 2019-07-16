/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.utils;

import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wangwendi
 * @version $Id: AsynExecuterUtil.java, v 0.1 2018年09月30日  wangwendi Exp $
 */
public class AsynExecuterUtil {

    private static Map<Class<? extends AsynCmd>,AbstractAsynExecuter<? extends AsynCmd>> asynExecuterMap = new ConcurrentHashMap<Class<? extends
            AsynCmd>, AbstractAsynExecuter<? extends AsynCmd>>();

    private static Map<String,Class<? extends AsynCmd>> asynCmdNameMapping = new ConcurrentHashMap<String, Class<? extends AsynCmd>>();

    public static Map<Class<? extends AsynCmd>, AbstractAsynExecuter<? extends AsynCmd>> getAsynExecuterMap() {
        return asynExecuterMap;
    }

    public static Map<String, Class<? extends AsynCmd>> getAsynCmdNameMapping() {
        return asynCmdNameMapping;
    }
}