package com.asyncmd.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangwendi
 * @date 2019/8/13
 */
@ConfigurationProperties(prefix = "asyn.executerjob")
public class AsynExecuterJobProperties {

    /**
     * 执行任务执行频率 默认每隔1秒执行一次
     */
    private String cron;

    /**
     * 异步命令是否要先进后出 默认先进先出 注意这个只针对一张表的情况
     */
    private Boolean desc;

    /**
     * 一次从表中捞取命令数量
     */
    private Integer limit;

    /**
     * 重试次数
     */
    private Integer retryNum;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Boolean getDesc() {
        return desc;
    }

    public void setDesc(Boolean desc) {
        this.desc = desc;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getRetryNum() {
        return retryNum;
    }

    public void setRetryNum(Integer retryNum) {
        this.retryNum = retryNum;
    }
}