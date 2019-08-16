
package com.asyncmd.config;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.manager.job.DispatchBackupJob;
import com.asyncmd.manager.job.DispatchExecuterJob;
import com.asyncmd.manager.job.DispatchRestJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronExpression;
import org.springframework.util.StringUtils;

/**
 * @author wangwendi
 * @version $Id: AsynJobConfig.java, v 0.1 2019年07月16日 wangwendi Exp $
 */
public class AsynJobConfig {
    private static Log log = LogFactory.getLog(AsynJobConfig.class);


    private static final String EXECUTE = "_execute";
    private static final String REST = "_rest";
    private static final String BACKUP = "_backup";

    /**
     * zookeeper地址
     */
    private String zookeeperUrl;
    /**
     * 调度任务名称 不同应用需要不同
     */
    private String jobName;
    /**
     * 执行任务执行频率 默认每隔1秒执行一次
     */
    private String cron = "0/1 * * * * ?";

    /**
     * 重置状态任务执行频率 默认每隔1分钟执行一次
     */
    private String restCron = "0 0/1 * * * ?";

    private AsynBackupConfig asynBackupConfig = new AsynBackupConfig();



    public void init(int tableNum,String env){

        try {
            //效验cron表达式是否正确
            CronExpression.validateExpression(cron);
            CronExpression.validateExpression(restCron);
            if (asynBackupConfig.getBackup()){
                CronExpression.validateExpression(asynBackupConfig.getBackupCron());
            }
        }catch (Exception e){
            log.error(AsynExCode.CRON_ILLEGAL.getMessage());
            throw new AsynException(AsynExCode.CRON_ILLEGAL);
        }
        if (StringUtils.isEmpty(zookeeperUrl)){
            log.error(AsynExCode.ZOOKEEPER_NULL.getMessage());
            throw new AsynException(AsynExCode.ZOOKEEPER_NULL);
        }
        if (StringUtils.isEmpty(jobName)){
            log.error(AsynExCode.JOB_NAME_NULL.getMessage());
            throw new AsynException(AsynExCode.JOB_NAME_NULL);
        }

        //如果没有分表 则设置1个分片
        if (tableNum == 0){
            tableNum = 1;
        }
        createExecuterJob(tableNum,env);
        createRestJob(tableNum,env);
        createBackupJob(tableNum,env);

    }

    /**
     * 创建执行任务
     * @param tableNum
     */
    private void createExecuterJob(int tableNum,String env){
        String newJobName = jobName + EXECUTE + env;
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(newJobName, cron, tableNum).build();
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, DispatchExecuterJob.class.getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
        new JobScheduler(createRegistryCenter(zookeeperUrl), simpleJobRootConfig).init();
    }

    /**
     * 创建状态重置任务 防止因为系统异常导致执行失败的任务一直无法变成初始化状态
     * @param tableNum
     */
    private void createRestJob(int tableNum,String env){
        String newJobName = jobName + REST + env;
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(newJobName, restCron, tableNum).build();
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, DispatchRestJob.class.getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
        new JobScheduler(createRegistryCenter(zookeeperUrl), simpleJobRootConfig).init();
    }

    /**
     * 创建备份任务
     * @param tableNum
     */
    private void createBackupJob(int tableNum,String env){
        //判断是否需要启动备份任务
        if (!asynBackupConfig.getBackup()){
            return;
        }
        String newJobName = jobName + BACKUP + env;
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(newJobName, asynBackupConfig.getBackupCron(), tableNum).build();
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, DispatchBackupJob.class.getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
        new JobScheduler(createRegistryCenter(zookeeperUrl), simpleJobRootConfig).init();
    }

    private static CoordinatorRegistryCenter createRegistryCenter(String zookeeperUrl) {
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration(zookeeperUrl, "asyn-executer"));
        regCenter.init();
        return regCenter;
    }

    public void setZookeeperUrl(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public void setRestCron(String restCron) {
        this.restCron = restCron;
    }

    public AsynBackupConfig getAsynBackupConfig() {
        return asynBackupConfig;
    }
}