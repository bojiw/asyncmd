/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.utils;

import com.asyncmd.service.DispatchService;

import java.util.Map;

/**
 * @author wangwendi
 * @version $Id: DispatchFactory.java, v 0.1 2019年07月19日 下午11:57 wangwendi Exp $
 */
public class DispatchFactory {

    private Map<String,DispatchService> dispatchServiceMap;

    public DispatchService getDispatchService(String dispatchMode){
        return dispatchServiceMap.get(dispatchMode);
    }


    public void setDispatchServiceMap(Map<String, DispatchService> dispatchServiceMap) {
        this.dispatchServiceMap = dispatchServiceMap;
    }
}