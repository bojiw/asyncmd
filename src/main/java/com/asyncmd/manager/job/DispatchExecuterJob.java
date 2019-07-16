/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.manager.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @author wangwendi
 * @version $Id: DispatchExecuterJob.java, v 0.1 2019年07月16日 上午9:49 wangwendi Exp $
 */
public class DispatchExecuterJob implements SimpleJob {

    public void execute(ShardingContext shardingContext) {
        System.out.println(shardingContext.getShardingItem());
    }
}