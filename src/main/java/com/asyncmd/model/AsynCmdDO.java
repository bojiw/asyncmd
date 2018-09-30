/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 异步命令DO对象
 * @author wangwendi
 * @version $Id: AsynCmdDO.java, v 0.1 2018年09月20日 wangwendi Exp $
 */
public class AsynCmdDO implements Serializable {
    private static final long serialVersionUID = -6139216780036677940L;

    /**
     * 唯一id
     */
    private String cmdId;

    /**
     * 命令类型
     */
    private String cmdType;

    /**
     * 业务id
     */
    private String bizId;

    /**
     * 业务上下文
     */
    private String content;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtMobile;

    /**
     * 执行次数
     */
    private Integer executeNum;

    /**
     * 下一次执行时间
     */
    private Date nextTime;

    /**
     * @see com.asyncmd.enums.AsynStatus
     *
     * 状态
     */
    private String status;

    /**
     * 创建任务的主机名
     */
    private String createHostname;

    /**
     * 创建任务的ip
     */
    private String createIp;

    /**
     * 创建者
     */
    private String createName;

    /**
     * 更新任务的主机名
     */
    private String updateHostname;

    /**
     * 更新任务的ip
     */
    private String updateIp;

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtMobile() {
        return gmtMobile;
    }

    public void setGmtMobile(Date gmtMobile) {
        this.gmtMobile = gmtMobile;
    }

    public Integer getExecuteNum() {
        return executeNum;
    }

    public void setExecuteNum(Integer executeNum) {
        this.executeNum = executeNum;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateHostname() {
        return createHostname;
    }

    public void setCreateHostname(String createHostname) {
        this.createHostname = createHostname;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getUpdateHostname() {
        return updateHostname;
    }

    public void setUpdateHostname(String updateHostname) {
        this.updateHostname = updateHostname;
    }

    public String getUpdateIp() {
        return updateIp;
    }

    public void setUpdateIp(String updateIp) {
        this.updateIp = updateIp;
    }
}