/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.service.impl;

import com.asyncmd.dao.AsynCmdDAO;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynCmdDO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wangwendi
 * @version $Id: AsynExecuterService.java, v 0.1 2018年09月30日 wangwendi Exp $
 */
public class AsynExecuterService {

    private Map<Class<? extends AsynCmd>,AbstractAsynExecuter<? extends AsynCmd>> asynExecuterMap = new ConcurrentHashMap<Class<? extends
            AsynCmd>, AbstractAsynExecuter<? extends AsynCmd>>();

    private AsynCmdDAO asynCmdDAO;

    /**
     * 插入异步命令
     * @param asynCmd
     * @return
     */
    public boolean saveCmd(AsynCmd asynCmd){
        AsynCmdDO asynCmdDO = new AsynCmdDO(asynCmd);
        return asynCmdDAO.saveCmd(asynCmdDO);
    }

    public Map<Class<? extends AsynCmd>, AbstractAsynExecuter<? extends AsynCmd>> getAsynExecuterMap() {
        return asynExecuterMap;
    }

    public void setAsynExecuterMap(
            Map<Class<? extends AsynCmd>, AbstractAsynExecuter<? extends AsynCmd>> asynExecuterMap) {
        this.asynExecuterMap = asynExecuterMap;
    }

    public AsynCmdDAO getAsynCmdDAO() {
        return asynCmdDAO;
    }

    public void setAsynCmdDAO(AsynCmdDAO asynCmdDAO) {
        this.asynCmdDAO = asynCmdDAO;
    }

}