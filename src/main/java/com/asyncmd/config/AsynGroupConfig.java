/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.config;

import com.asyncmd.utils.ThreadPoolTaskExecutorUtil;
import com.asyncmd.utils.TransactionTemplateUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author wangwendi
 * @version $Id: AsynGroupConfig.java, v 0.1 2019年07月16日 下午2:01 wangwendi Exp $
 */
public class AsynGroupConfig {
    private AsynConfig asynConfig;


    public AsynGroupConfig(){
        asynConfig = new AsynConfig();
    }

    public void init(){
        asynConfig.initConfig();
    }


    public AsynConfig getAsynConfig(){
        return asynConfig;
    }


    public void setTemplate(TransactionTemplate template){
        TransactionTemplateUtil.newInstance().setTransactionTemplate(template);
    }

    public void setPoolTaskExecutor(ThreadPoolTaskExecutor poolTaskExecutor) {
        ThreadPoolTaskExecutorUtil.newInstance().setPoolTaskExecutor(poolTaskExecutor);
    }

    public void setExecuterFrequencys(String executerFrequencys) {
        asynConfig.setExecuterFrequencys(executerFrequencys);
    }

    public void setTableNum(int tableNum) {
        asynConfig.setTableNum(tableNum);
    }

    public void setZookeeperUrl(String zookeeperUrl){
        asynConfig.getAsynJobConfig().setZookeeperUrl(zookeeperUrl);
    }

    public void setJobName(String jobName) {
        asynConfig.getAsynJobConfig().setJobName(jobName);
    }

    public void setCron(String cron) {
        asynConfig.getAsynJobConfig().setCron(cron);
    }

    public void setLimit(int limit) {
        asynConfig.setLimit(limit);
    }
}