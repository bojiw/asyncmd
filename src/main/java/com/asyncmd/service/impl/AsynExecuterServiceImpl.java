/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.service.impl;

import com.asyncmd.dao.AsynCmdDAO;
import com.asyncmd.dao.AsynCmdHistoryDAO;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynCmdDO;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.utils.TransactionTemplateUtil;
import com.asyncmd.utils.convert.AsynCmdConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wangwendi
 * @version $Id: AsynExecuterService.java, v 0.1 2018年09月30日 wangwendi Exp $
 */
@Service
public class AsynExecuterServiceImpl  implements AsynExecuterService {

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
                asynCmdDAO.delCmd(asynCmd.getBizId());
                asynCmdHistoryDAO.saveCmd(AsynCmdConvert.toHistoryCmd(asynCmd));
            }
        });
    }

    public boolean updateStatus(List<String> bizIds, AsynStatus asynStatus) {
        long l = asynCmdDAO.batchUpdateStatus(bizIds, asynStatus.getStatus());
        if (l > 0){
            return true;
        }
        return false;

    }

    public List<AsynCmd> queryAsynCmd(int limit) {
        List<AsynCmdDO> asynCmdDOS = asynCmdDAO.queryAsynCmd(limit, new Date(System.currentTimeMillis() + 1000));
        return AsynCmdConvert.toCmdList(asynCmdDOS);
    }

    public List<AsynCmd> querySubTableAsynCmd(int limit, int tableIndex) {
        return null;
    }

}