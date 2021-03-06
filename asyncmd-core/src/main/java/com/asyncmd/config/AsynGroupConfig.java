
package com.asyncmd.config;

import com.asyncmd.callback.AbstractErrorCallBack;
import com.asyncmd.callback.ErrorCallBack;
import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.utils.JdbcTemplateUtil;
import com.asyncmd.utils.ThreadPoolTaskExecutorUtil;
import com.asyncmd.utils.TransactionTemplateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;

/**
 * @author wangwendi
 * @version $Id: AsynGroupConfig.java, v 0.1 2019年07月16日 wangwendi Exp $
 */
public class AsynGroupConfig {
    private static Log log = LogFactory.getLog(AsynGroupConfig.class);

    private AsynConfig asynConfig;




    public AsynGroupConfig(){
        asynConfig = new AsynConfig();
    }

    public void init(){
        if (JdbcTemplateUtil.newInstance().getJdbcTemplate().getDataSource() == null){
            log.error(AsynExCode.DATASOURCE_NULL.getMessage());
            throw new AsynException(AsynExCode.DATASOURCE_NULL);
        }
        asynConfig.initConfig();
    }


    public AsynConfig getAsynConfig(){
        return asynConfig;
    }



    public void setCorePoolSize(int corePoolSize){
        ThreadPoolTaskExecutorUtil.newInstance().setCorePoolSize(corePoolSize);
    }
    public void setMaxPoolSize(int maxPoolSize){
        ThreadPoolTaskExecutorUtil.newInstance().setMaxPoolSize(maxPoolSize);
    }
    public void setQueueCapacity(int queueCapacity){
        ThreadPoolTaskExecutorUtil.newInstance().setQueueCapacity(queueCapacity);
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
    public void setRetryNum(int retryNum) {
        asynConfig.setRetryNum(retryNum);
    }

    public void setRestCron(String restCron){
        asynConfig.getAsynJobConfig().setRestCron(restCron);
    }
    public void setDesc(Boolean desc) {
        asynConfig.setDesc(desc);
    }

    public void setDataSource(DataSource dataSource) {
        JdbcTemplateUtil.newInstance().setDataSource(dataSource);
        TransactionTemplateUtil.newInstance().setDataSource(dataSource);
    }

    public void setMaxNo(Integer maxNo) {
        asynConfig.getAsynJobConfig().getAsynBackupConfig().setMaxNo(maxNo);
    }

    public void setBeforeDate(Integer beforeDate) {
        asynConfig.getAsynJobConfig().getAsynBackupConfig().setBeforeDate(beforeDate);
    }

    public void setBackup(Boolean backup) {
        asynConfig.getAsynJobConfig().getAsynBackupConfig().setBackup(backup);
    }


    public void setBackupCron(String backupCron) {
        asynConfig.getAsynJobConfig().getAsynBackupConfig().setBackupCron(backupCron);
    }

    public void setEnv(String env){
        asynConfig.setEnv(env);
    }


    public void setAbstractErrorCallBack(AbstractErrorCallBack abstractErrorCallBack) {
        asynConfig.setAbstractErrorCallBack(abstractErrorCallBack);
    }
}