
package com.asyncmd.manager.job;

import com.asyncmd.manager.AsynBackupJobManager;
import com.asyncmd.utils.AsynSpringUtil;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @author wangwendi
 * @version $Id: DispatchBackupJob.java, v 0.1 2019年07月23日 wangwendi Exp $
 */
public class DispatchBackupJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        AsynBackupJobManager asynBackupJobManager = AsynSpringUtil.getBean(AsynBackupJobManager.class);
        asynBackupJobManager.backup(shardingContext.getShardingItem());
    }
}