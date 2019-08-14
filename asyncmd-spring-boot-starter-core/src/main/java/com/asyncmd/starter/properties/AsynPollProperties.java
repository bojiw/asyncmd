package com.asyncmd.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangwendi
 * @date 2019/8/13
 */
@ConfigurationProperties(prefix = "asyn.poll")
public class AsynPollProperties {

    /**
     * 最大线程数 默认150
     */
    private Integer maxPoolSize;

    /**
     * 缓存队列长度 默认300
     */
    private Integer queueCapacity;

    /**
     * 最小线程池 默认10
     */
    private Integer corePoolSize;

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
}