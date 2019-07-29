
package com.asyncmd.config;

/**
 * 备份任务配置信息
 * @author wangwendi
 * @version $Id: AsynBackupConfig.java, v 0.1 2019年07月23日 下午12:28 wangwendi Exp $
 */
public class AsynBackupConfig {

    /**
     * 最大处理页数 一页1000条
     */
    private Integer maxNo = 100;

    /**
     * 默认处理30天以前的执行成功历史数据 单位为天
     */
    private Integer beforeDate = 30;

    /**
     * 是否开启自动备份功能
     */
    private Boolean backup = false;

    /**
     * 每天凌晨2点执行一次备份
     */
    private String backupCron = "0 0 2 * * ? ";

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