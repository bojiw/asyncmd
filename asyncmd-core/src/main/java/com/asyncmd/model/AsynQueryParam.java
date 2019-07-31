/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.model;

import java.util.Date;

/**
 * 异步命令查询请求
 * @author wangwendi
 * @version $Id: AsynQueryParam.java, v 0.1 2019年07月31日 下午7:35 wangwendi Exp $
 */
public class AsynQueryParam {
    /**
     * 状态
     */
    private String status;
    /**
     * 查询条数
     */
    private Integer limit;
    /**
     * 多旧执行内的数据
     */
    private Date executerTime;
    /**
     * 是否后进先出
     */
    private Boolean desc;
    /**
     * 所属环境
     */
    private String env;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Date getExecuterTime() {
        return executerTime;
    }

    public void setExecuterTime(Date executerTime) {
        this.executerTime = executerTime;
    }

    public Boolean getDesc() {
        return desc;
    }

    public void setDesc(Boolean desc) {
        this.desc = desc;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}