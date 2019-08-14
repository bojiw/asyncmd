package com.asyncmd.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangwendi
 * @date 2019/8/13
 */
@ConfigurationProperties(prefix = "asyn.backupjob")
public class AsynBackupJobProperties {

    /**
     * 最大处理页数 一页1000条
     */
    private Integer maxNo;

    /**
     * 默认处理30天以前的执行成功历史数据 单位为天
     */
    private Integer beforeDate;

    /**
     * 是否开启自动备份功能
     */
    private Boolean backup;

    /**
     * 每天凌晨2点执行一次备份
     */
    private String backupCron;

    public Integer getMaxNo() {
        return maxNo;
    }

    public void setMaxNo(Integer maxNo) {
        this.maxNo = maxNo;
    }

    public Integer getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(Integer beforeDate) {
        this.beforeDate = beforeDate;
    }

    public Boolean getBackup() {
        return backup;
    }

    public void setBackup(Boolean backup) {
        this.backup = backup;
    }

    public String getBackupCron() {
        return backupCron;
    }

    public void setBackupCron(String backupCron) {
        this.backupCron = backupCron;
    }
}