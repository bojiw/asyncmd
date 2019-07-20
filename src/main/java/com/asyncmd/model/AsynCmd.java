/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.model;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * 异步命令对象
 * @author wangwendi
 * @version $Id: AsynCmd.java, v 0.1 2018年09月20日 下午7:36 wangwendi Exp $
 */
public abstract class AsynCmd<E extends AsynBizObject> implements Serializable{
    private static final long serialVersionUID = 2362119134833203155L;

    public static final String default_create_name = "system";

    /**
     * 唯一id 需要唯一 如果业务没有好的唯一算法 可以使用SnowflakeIdWorkerUtil工具类生成
     */
    private Long cmdId;

    /**
     * 命令类型
     */
    private String cmdType;

    /**
     * 业务id
     */
    private String bizId;


    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModify;

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

    /**
     * 上下文对象
     */
    private E content;

    /**
     * 执行成功的处理器名
     */
    private List<String> successExecuters = Lists.newArrayList();

    /**
     * 获取范型类型 由子类返回
     * @return
     */
    protected abstract Class<E> getObject();

    /**
     * json转对象
     * @param content
     * @return
     */
    public E jsonToObject(String content){
        return JSON.parseObject(content,getObject());
    }

    /**
     * 对象转json
     * @param content
     * @return
     */
    public String objectToJson(E content){
        return JSON.toJSONString(content);
    }


    public Long getCmdId() {
        return cmdId;
    }

    public void setCmdId(Long cmdId) {
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

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
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

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public E getContent() {
        return content;
    }

    public void setContent(E content) {
        this.content = content;
    }

    public List<String> getSuccessExecuters() {
        return successExecuters;
    }

    public void setSuccessExecuters(List<String> successExecuters) {
        this.successExecuters = successExecuters;
    }

    public void addSuccessExecuter(String successExecuter){
        successExecuters.add(successExecuter);
    }
}