/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.service.impl;

import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynExecuter;
import com.asyncmd.service.AsynExecuterService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wangwendi
 * @version $Id: AsynExecuterServiceImpl.java, v 0.1 2018年09月30日 下午2:38 wangwendi Exp $
 */
public class AsynExecuterServiceImpl implements AsynExecuterService {

    private Map<Class<? extends AsynCmd>,AsynExecuter<? extends AsynCmd>> asynExecuterMap = new ConcurrentHashMap<Class<? extends
            AsynCmd>, AsynExecuter<? extends AsynCmd>>();



    public Map<Class<? extends AsynCmd>, AsynExecuter<? extends AsynCmd>> getAsynExecuterMap() {
        return asynExecuterMap;
    }

    public void setAsynExecuterMap(
            Map<Class<? extends AsynCmd>, AsynExecuter<? extends AsynCmd>> asynExecuterMap) {
        this.asynExecuterMap = asynExecuterMap;
    }
}