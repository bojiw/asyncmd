/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.model;

import java.util.List;

/**
 * 异步命令更新请求参数
 * @author wangwendi
 * @version $Id: AsynUpdateParam.java, v 0.1 2019年07月17日 下午6:39 wangwendi Exp $
 */
public class AsynUpdateParam {
    /**
     * 业务id
     */
    private List<String> bizIds;

    /**
     * 需要更新的状态
     * @see com.asyncmd.enums.AsynStatus
     */
    private String status;


    /**
     * 条件状态
     * @see com.asyncmd.enums.AsynStatus
     */
    private String whereAsynStatus;

    /**
     * 是否执行
     */
    private Boolean executer;

    /**
     * 是否重置
     */
    private Boolean reset;

    public List<String> getBizIds() {
        return bizIds;
    }

    public void setBizIds(List<String> bizIds) {
        this.bizIds = bizIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWhereAsynStatus() {
        return whereAsynStatus;
    }

    public void setWhereAsynStatus(String whereAsynStatus) {
        this.whereAsynStatus = whereAsynStatus;
    }

    public Boolean getExecuter() {
        return executer;
    }

    public void setExecuter(Boolean executer) {
        this.executer = executer;
    }

    public Boolean getReset() {
        return reset;
    }

    public void setReset(Boolean reset) {
        this.reset = reset;
    }
}