package com.asyncmd.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangwendi
 * @date 2019/8/13
 */
@ConfigurationProperties(prefix = "asyn.restjob")
public class AsynRestjobProperties {
    /**
     * 重置状态任务执行频率 默认每隔1分钟执行一次
     */
    private String restCron;

    public String getRestCron() {
        return restCron;
    }

    public void setRestCron(String restCron) {
        this.restCron = restCron;
    }
}