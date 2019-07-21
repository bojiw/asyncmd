/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.service.impl;

import com.asyncmd.config.AsynGroupConfig;
import com.asyncmd.dao.AsynCmdDAO;
import com.asyncmd.dao.AsynCmdHistoryDAO;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynCmdDO;
import com.asyncmd.model.AsynUpdateParam;
import com.asyncmd.model.Frequency;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.utils.AsynExecuterUtil;
import com.asyncmd.utils.TransactionTemplateUtil;
import com.asyncmd.utils.convert.AsynCmdConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.util.CollectionUtils;

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
    @Autowired
    private AsynGroupConfig asynGroupConfig;
    /**
     * 插入异步命令
     * @param asynCmd
     * @return
     */
    public long saveCmd(AsynCmd asynCmd){
        AsynCmdDO asynCmdDO = AsynCmdConvert.toDo(asynCmd);

        if (asynGroupConfig.getAsynConfig().isSubTable()){
            int index = getIndex(asynCmd.getBizId());
            return asynCmdDAO.saveCmdSubTable(asynCmdDO,getTableIndex(index));
        }else {
            return asynCmdDAO.saveCmd(asynCmdDO);
        }
    }


    /**
     * 根据业务id计算分配到哪个表中
     * @param bizId
     * @return
     */
    private int getIndex(String bizId){
        return (asynGroupConfig.getAsynConfig().getTableNum() - 1) & hash(bizId);
    }

    private int hash(String bizId){
        int h;
        return (h = bizId.hashCode()) ^ (h >>> 16);
    }


    /**
     * 备份异步命令
     * @param asynCmd
     */
    public void backupCmd(final AsynCmd asynCmd){
        //重新捞取最新的一条数据
        final AsynCmd newAsynCmd = getAsynCmdByBizId(asynCmd.getBizId(), asynCmd.getClass());
        if (newAsynCmd == null){
            return;
        }
        TransactionTemplateUtil.newInstance().getTemplate().execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                if (asynGroupConfig.getAsynConfig().isSubTable()){
                    int index = getIndex(newAsynCmd.getBizId());
                    asynCmdDAO.delCmdSubTable(getTableIndex(index),newAsynCmd.getBizId());
                    asynCmdHistoryDAO.saveCmdSubTable(getTableIndex(index),AsynCmdConvert.toHistoryCmd(newAsynCmd));
                }else {
                    asynCmdDAO.delCmd(newAsynCmd.getBizId());
                    asynCmdHistoryDAO.saveCmd(AsynCmdConvert.toHistoryCmd(newAsynCmd));
                }

            }
        });
    }

    public boolean updateStatus(AsynUpdateParam param) {
        Long sum;
        if (asynGroupConfig.getAsynConfig().isSubTable()){
            sum = asynCmdDAO.updateStatusSubTable(getTableIndex(getIndex(param.getBizId())),param);
        }else {
            sum = asynCmdDAO.updateStatus(param);

        }
        if (sum > 0){
            return true;
        }
        return false;
    }

    public boolean batchUpdateStatus(AsynUpdateParam param, Integer tableIndex) {
        Long sum;

        if (asynGroupConfig.getAsynConfig().getTableNum() == 0){
            sum = asynCmdDAO.batchUpdateStatus(param);
        }else {
            sum = asynCmdDAO.batchupdateStatusSubTable(getTableIndex(tableIndex), param);

        }
        if (sum > 0){
            return true;
        }
        return false;

    }

    public List<AsynCmd> queryAsynCmd(int limit,int tableIndex,AsynStatus status,Date whereNextTime) {
        List<AsynCmdDO> asynCmdDOS;
        Boolean desc = asynGroupConfig.getAsynConfig().getDesc();
        if (asynGroupConfig.getAsynConfig().isSubTable()){
            asynCmdDOS = asynCmdDAO.querySubTableAsynCmd(getTableIndex(tableIndex),status.getStatus(),limit,whereNextTime,desc);
        }else {
            asynCmdDOS = asynCmdDAO.queryAsynCmd(status.getStatus(),limit, whereNextTime,desc);
        }
        return AsynCmdConvert.toCmdList(asynCmdDOS);
    }


    private String getTableIndex(int tableIndex){
        if (tableIndex < 10){
            return "0" + tableIndex;
        }
        return String.valueOf(tableIndex);
    }

    public Date getNextTime(List<Frequency> executerFrequencyList,AsynCmd asynCmd) {
        int retry = asynCmd.getExecuteNum() - 1;
        if (!CollectionUtils.isEmpty(executerFrequencyList)){
            return nextTime(retry,executerFrequencyList,asynCmd);
        }
        List<Frequency> groupExecuterFrequencys = asynGroupConfig.getAsynConfig().getExecuterFrequency();
        return nextTime(retry,groupExecuterFrequencys,asynCmd);
    }

    private Date nextTime(int retry,List<Frequency> executerFrequencyList,AsynCmd asynCmd){
        Frequency frequency;
        if (executerFrequencyList.size() < retry){
            frequency = executerFrequencyList.get(executerFrequencyList.size() - 1);
        }else {
            frequency = executerFrequencyList.get(retry);
        }
        return frequency.getNextTime(asynCmd.getNextTime());
    }

    public AsynCmd getAsynCmdByBizId(String bizId,Class<? extends AsynCmd> asynCmdClass) {
        if (asynGroupConfig.getAsynConfig().isSubTable()){
            int index = getIndex(bizId);
            return AsynCmdConvert.toCmd(asynCmdDAO.getSubTableAsynCmdByBizId(getTableIndex(index), bizId),asynCmdClass);
        }
        return AsynCmdConvert.toCmd(asynCmdDAO.getAsynCmdByBizId(bizId),asynCmdClass);
    }
}