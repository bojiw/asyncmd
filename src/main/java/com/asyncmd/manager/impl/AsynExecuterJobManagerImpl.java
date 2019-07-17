/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.manager.impl;

import com.asyncmd.config.AsynGroupConfig;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.manager.AsynExecuterJobManager;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynUpdateParam;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.utils.AsynExecuterUtil;
import com.asyncmd.utils.ThreadPoolTaskExecutorUtil;
import com.asyncmd.utils.TransactionTemplateUtil;
import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author wangwendi
 * @version $Id: AsynExecuterJobManagerImpl.java, v 0.1 2019年07月16日 下午7:23 wangwendi Exp $
 */
@Service
public class AsynExecuterJobManagerImpl implements AsynExecuterJobManager {
    private static Log log = LogFactory.getLog(AsynExecuterJobManagerImpl.class);


    @Autowired
    private AsynExecuterService asynExecuterService;

    @Autowired
    private AsynGroupConfig asynGroupConfig;

    public void executer(int tableIndex) {

        asynExecuter(tableIndex);
    }


    /**
     * 异步命令执行
     */
    private void asynExecuter(final int tableIndex){
        if (asynGroupConfig.getAsynConfig().getLimit() > getPoolSurplusNum()){
            log.warn("asyn线程池不足,请注意,剩余线程数"+ getPoolSurplusNum());
            return;
        }
        final List<AsynCmd> asynCmdList = asynExecuterService.queryAsynCmd(asynGroupConfig.getAsynConfig().getLimit(),tableIndex,AsynStatus.INIT);
        final List<String> bizIds = getBizIds(asynCmdList);
        if (CollectionUtils.isEmpty(bizIds)){
            return;
        }
        //更新状态为执行中
        final List<String> failBizIds = TransactionTemplateUtil.newInstance().getTemplate().execute(new TransactionCallback<List<String>>() {
            public List<String> doInTransaction(TransactionStatus status) {
                AsynUpdateParam param = new AsynUpdateParam();
                param.setBizIds(bizIds);
                param.setExecuter(true);
                param.setStatus(AsynStatus.EXECUTE.getStatus());
                param.setWhereAsynStatus(AsynStatus.INIT.getStatus());
                //如果更新失败 则直接返回
                boolean b = asynExecuterService.batchUpdateStatus(param, tableIndex);
                if (!b){
                    return Lists.newArrayList();
                }
                List<AsynCmd> failList = pushPool(asynCmdList);
                //push线程池失败的异步命令业务id
                return getBizIds(failList);
            }
        });
        if (CollectionUtils.isEmpty(failBizIds)){
            return;
        }
        //推送线程池失败的命令状态重置
        TransactionTemplateUtil.newInstance().getTemplate().execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                AsynUpdateParam failParam = new AsynUpdateParam();
                failParam.setBizIds(failBizIds);
                failParam.setReset(true);
                failParam.setStatus(AsynStatus.INIT.getStatus());
                failParam.setWhereAsynStatus(AsynStatus.EXECUTE.getStatus());
                asynExecuterService.batchUpdateStatus(failParam,tableIndex);
            }
        });
    }


    private int getPoolSurplusNum(){
        ThreadPoolTaskExecutor poolTaskExecutor = ThreadPoolTaskExecutorUtil.newInstance().getPoolTaskExecutor();
        return poolTaskExecutor.getMaxPoolSize() - poolTaskExecutor.getActiveCount();
    }

    private List<String> getBizIds(List<AsynCmd> asynCmdList){
        if (CollectionUtils.isEmpty(asynCmdList)){
            return Lists.newArrayList();
        }
        List<String> bizIds = Lists.newArrayList();
        for (AsynCmd asynCmd : asynCmdList){
            bizIds.add(asynCmd.getBizId());
        }
        return bizIds;
    }


    private List<AsynCmd> pushPool(List<AsynCmd> asynCmdList){
        List<AsynCmd> failList = Lists.newArrayList();
        for (AsynCmd asynCmd : asynCmdList){
            AbstractAsynExecuter<? extends AsynCmd> abstractAsynExecuter = AsynExecuterUtil.getAsynExecuterMap().get(asynCmd.getClass());
            if (!abstractAsynExecuter.asynExecuter(asynCmd)){
                failList.add(asynCmd);
            }
        }
        return failList;
    }

    public void reset(int tableIndex) {

    }
}