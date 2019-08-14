package com.asyncmd.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangwendi
 * @date 2019/8/13
 */
@ConfigurationProperties(prefix = "asyn.core")
public class AsynCoreProperties {
    /**
     * zookeeper地址
     */
    private String zookeeperUrl;
    /**
     * 调度任务名称 不同应用需要不同
     */
    private String jobName;

    /**
     * 所在环境 主要为了解决预发环境只处理预发生产的命令 正式环境只处理正式环境的命令 本地运行和开发环境也可以做区分
     */
    private String env;
    /**
     * 分表数量 如果设置需要是2的倍数如 8 16 32 64
     */
    private Integer tableNum;
    /**
     * 重试执行频率 5s,10s,1m,1h
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次1分钟以后执行 第四次1小时以后执行 之后都是间隔1小时执行
     * 执行频率 5s,10s,20s
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次20秒以后执行
     */
    private String executerFrequencys;

    public String getZookeeperUrl() {
        return zookeeperUrl;
    }

    public void setZookeeperUrl(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Integer getTableNum() {
        return tableNum;
    }

    public void setTableNum(Integer tableNum) {
        this.tableNum = tableNum;
    }

    public String getExecuterFrequencys() {
        return executerFrequencys;
    }

    public void setExecuterFrequencys(String executerFrequencys) {
        this.executerFrequencys = executerFrequencys;
    }
}