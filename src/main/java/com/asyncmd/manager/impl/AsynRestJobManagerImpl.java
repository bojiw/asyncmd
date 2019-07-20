/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.manager.impl;

import com.asyncmd.config.AsynGroupConfig;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.manager.AsynRestJobManager;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynUpdateParam;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.utils.AsynExecuterUtil;
import com.asyncmd.utils.TransactionTemplateUtil;
import com.asyncmd.utils.convert.AsynCmdConvert;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 *
 * @author wangwendi
 * @version $Id: AsynRestJobManagerImpl.java, v 0.1 2019年07月18日 上午10:07 wangwendi Exp $
 */
@Service
public class AsynRestJobManagerImpl implements AsynRestJobManager {

    @Autowired
    private AsynExecuterService asynExecuterService;

    @Autowired
    private AsynGroupConfig asynGroupConfig;

    public void rest(int tableIndex) {
        //把已经执行1分钟的异步命令查询出来
        List<AsynCmd> asynCmdList = asynExecuterService.queryAsynCmd(100, tableIndex, AsynStatus.EXECUTE,getRestDate());
        if (CollectionUtils.isEmpty(asynCmdList)){
            return;
        }
        //把查询出来的异步命令分成两组 一组为状态重置为失败状态 一组为状态重置为初始化状态
        final List<AsynUpdateParam> executeUpdates = Lists.newArrayList();
        List<AsynCmd> error = Lists.newArrayList();
        for (AsynCmd asynCmd : asynCmdList){
            if (asynCmd.getExecuteNum() <= asynGroupConfig.getAsynConfig().getRetryNum()){
                AsynUpdateParam asynUpdateParam = new AsynUpdateParam();
                asynUpdateParam.setBizId(asynCmd.getBizId());
                asynUpdateParam.setStatus(AsynStatus.INIT.getStatus());
                asynUpdateParam.setWhereAsynStatus(AsynStatus.EXECUTE.getStatus());
                List<AbstractAsynExecuter<? extends AsynCmd>> abstractAsynExecuters = AsynExecuterUtil.getAsynExecuterMap().get(asynCmd.getClass());
                asynUpdateParam.setNextTime(asynExecuterService.getNextTime(abstractAsynExecuters.get(0).getExecuterFrequencyList(),asynCmd));
                executeUpdates.add(asynUpdateParam);
            }else {
                error.add(asynCmd);
            }

        }
        if (!CollectionUtils.isEmpty(error)){
            List<String> errorBizIds = AsynCmdConvert.toBizIds(error);
            AsynUpdateParam param = new AsynUpdateParam();
            param.setBizIds(errorBizIds);
            param.setStatus(AsynStatus.ERROR.getStatus());
            param.setWhereAsynStatus(AsynStatus.EXECUTE.getStatus());
            asynExecuterService.batchUpdateStatus(param,tableIndex);
        }
        //事务更新 提高更新速度
        if (!CollectionUtils.isEmpty(executeUpdates)){
            TransactionTemplateUtil.newInstance().getTemplate().execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    for (AsynUpdateParam asynUpdateParam : executeUpdates){
                        asynExecuterService.updateStatus(asynUpdateParam);
                    }
                }
            });
        }



    }


    private Date getRestDate(){
        return new Date(System.currentTimeMillis() - 60000);
    }


}