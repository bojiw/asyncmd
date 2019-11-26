
package com.asyncmd.manager.job;

import com.asyncmd.manager.AsynRestJobManager;
import com.asyncmd.utils.AsynSpringUtil;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * 调度重置任务 防止因为系统异常导致执行失败的任务一直无法变成初始化状态
 * @author wangwendi
 * @version $Id: DispatchRestJob.java, v 0.1 2019年07月18日 上午9:50 wangwendi Exp $
 */
public class DispatchRestJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        AsynRestJobManager asynRestJobManager = AsynSpringUtil.getBean(AsynRestJobManager.class);
        asynRestJobManager.rest(shardingContext.getShardingItem());
    }
}