/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.config;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.manager.job.DispatchExecuterJob;
import com.asyncmd.manager.job.DispatchRestJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.quartz.CronExpression;

/**
 * @author wangwendi
 * @version $Id: AsynJobConfig.java, v 0.1 2019年07月16日 上午11:24 wangwendi Exp $
 */
public class AsynJobConfig {

    private static final String EXECUTE = "_execute";
    private static final String REST = "_rest";

    /**
     * zookeeper地址
     */
    private String zookeeperUrl;
    /**
     * 调度任务名称 不同应用需要不同
     */
    private String jobName;
    /**
     * 执行任务执行频率 默认每隔3秒执行一次
     */
    private String cron = "0/3 * * * * ?";

    /**
     * 重置状态任务执行频率 默认每隔60秒执行一次
     */
    private String restCron = "0/60 * * * * ?";


    public void init(int tableNum){

        try {
            //效验cron表达式是否正确
            CronExpression.validateExpression(cron);
        }catch (Exception e){
            throw new AsynException(AsynExCode.CRON_ILLEGAL);
        }

        //如果没有分表 则设置1个分片
        if (tableNum == 0){
            tableNum = 1;
        }
        createExecuterJob(tableNum);
        createRestJob(tableNum);
    }

    /**
     * 创建执行任务
     * @param tableNum
     */
    private void createExecuterJob(int tableNum){
        String newJobName = jobName + EXECUTE;
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
    private void createRestJob(int tableNum){
        String newJobName = jobName + REST;
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(newJobName, restCron, tableNum).build();
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, DispatchRestJob.class.getCanonicalName());
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
}