/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.service.impl;

import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;

import java.util.List;

/**
 * 异步执行
 * @author wangwendi
 * @version $Id: AsynDispatchServiceImpl.java, v 0.1 2019年07月19日 下午11:51 wangwendi Exp $
 */

public class AsynDispatchServiceImpl extends AbstractDispatchService{



    public void dispatch(AsynCmd asynCmd,List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList) {
        asynExecuter(asynCmd,asynExecuterList);
    }

    /**
     * 异步执行
     */
    public boolean asynExecuter(AsynCmd asynCmd, List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList){
        return super.poolAsynExecuter(asynCmd,asynExecuterList);
    }



}