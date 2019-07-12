/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.service;

import com.asyncmd.convert.AsynCmdConvert;
import com.asyncmd.dao.AsynCmdDAO;
import com.asyncmd.dao.AsynCmdHistoryDAO;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynCmdDO;
import com.asyncmd.model.AsynCmdHistoryDO;
import com.asyncmd.utils.TransactionTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wangwendi
 * @version $Id: AsynExecuterService.java, v 0.1 2018年09月30日 wangwendi Exp $
 */
@Service
public class AsynExecuterService {

    private Map<Class<? extends AsynCmd>,AbstractAsynExecuter<? extends AsynCmd>> asynExecuterMap = new ConcurrentHashMap<Class<? extends
            AsynCmd>, AbstractAsynExecuter<? extends AsynCmd>>();

    @Autowired
    private AsynCmdDAO asynCmdDAO;

    @Autowired
    private AsynCmdHistoryDAO asynCmdHistoryDAO;
    /**
     * 插入异步命令
     * @param asynCmd
     * @return
     */
    public long saveCmd(AsynCmd asynCmd){
        AsynCmdDO asynCmdDO = new AsynCmdDO(asynCmd);
        return asynCmdDAO.saveCmd(asynCmdDO);
    }

    /**
     * 备份异步命令
     * @param asynCmd
     */
    public void backupCmd(final AsynCmd asynCmd){
        TransactionTemplateUtil.newInstance().getTemplate().execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                asynCmdDAO.delCmd(asynCmd.getCmdId());
                asynCmdHistoryDAO.saveCmd(AsynCmdConvert.toHistoryCmd(asynCmd));
            }
        });
    }


    public Map<Class<? extends AsynCmd>, AbstractAsynExecuter<? extends AsynCmd>> getAsynExecuterMap() {
        return asynExecuterMap;
    }

    public void setAsynExecuterMap(
            Map<Class<? extends AsynCmd>, AbstractAsynExecuter<? extends AsynCmd>> asynExecuterMap) {
        this.asynExecuterMap = asynExecuterMap;
    }

}