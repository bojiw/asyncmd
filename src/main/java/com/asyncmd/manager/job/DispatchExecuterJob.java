/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.manager.job;

import com.asyncmd.manager.AsynExecuterJobManager;
import com.asyncmd.utils.AsynSpringUtil;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * 调度异步命令执行器
 * @author wangwendi
 * @version $Id: DispatchExecuterJob.java, v 0.1 2019年07月16日 上午9:49 wangwendi Exp $
 */
public class DispatchExecuterJob implements SimpleJob {

    public void execute(ShardingContext shardingContext) {
        AsynExecuterJobManager asynExecuterJobManager = AsynSpringUtil.getBean(AsynExecuterJobManager.class);
        System.out.println(shardingContext.getShardingItem());
        asynExecuterJobManager.executer(shardingContext.getShardingItem());
    }
}