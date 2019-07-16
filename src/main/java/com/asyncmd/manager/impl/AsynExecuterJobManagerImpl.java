/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.manager.impl;

import com.asyncmd.config.GroupConfig;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.manager.AsynExecuterJobManager;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.utils.AsynExecuterUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author wangwendi
 * @version $Id: AsynExecuterJobManagerImpl.java, v 0.1 2019年07月16日 下午7:23 wangwendi Exp $
 */
@Service
public class AsynExecuterJobManagerImpl implements AsynExecuterJobManager {

    @Autowired
    private AsynExecuterService asynExecuterService;

    @Autowired
    private GroupConfig groupConfig;

    public void executer(int tableIndex) {

        //如果没有分表
        if (groupConfig.getAsynConfig().getTableNum() == 0){
            List<AsynCmd> asynCmdList = asynExecuterService.queryAsynCmd(groupConfig.getAsynConfig().getLimit());
            if (CollectionUtils.isEmpty(asynCmdList)){
                return;
            }
            List<String> bizIds = Lists.newArrayList();
            for (AsynCmd asynCmd : asynCmdList){
                bizIds.add(asynCmd.getBizId());
            }
            //更新状态为执行中
            asynExecuterService.updateStatus(bizIds,AsynStatus.EXECUTE);
            List<AsynCmd> failList = pushPool(asynCmdList);
            List<String> failBizIds = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(failList)){
                for (AsynCmd asynCmd : failList){
                    failBizIds.add(asynCmd.getBizId());
                }
                asynExecuterService.updateStatus(failBizIds,AsynStatus.INIT);
            }


        }
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