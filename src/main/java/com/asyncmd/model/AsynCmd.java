
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
 * @version $Id: AsynCmd.java, v 0.1 2018年09月20日 wangwendi Exp $
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
    private String createHostName;

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
    private String updateHostName;

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
    final public E jsonToObject(String content){
        return JSON.parseObject(content,getObject());
    }

    /**
     * 对象转json
     * @param content
     * @return
     */
    final public String objectToJson(E content){
        return JSON.toJSONString(content);
    }


    final public Long getCmdId() {
        return cmdId;
    }

    final public void setCmdId(Long cmdId) {
        this.cmdId = cmdId;
    }

    final public String getCmdType() {
        return cmdType;
    }

    final public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    final public String getBizId() {
        return bizId;
    }

    final public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    final public Date getGmtCreate() {
        return gmtCreate;
    }

    final public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    final public Integer getExecuteNum() {
        return executeNum;
    }

    final public void setExecuteNum(Integer executeNum) {
        this.executeNum = executeNum;
    }

    final public Date getNextTime() {
        return nextTime;
    }

    final public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    final public String getStatus() {
        return status;
    }

    final public void setStatus(String status) {
        this.status = status;
    }

    final public String getCreateIp() {
        return createIp;
    }

    final public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    final public String getCreateName() {
        return createName;
    }

    final public void setCreateName(String createName) {
        this.createName = createName;
    }

    final public String getCreateHostName() {
        return createHostName;
    }

    final public void setCreateHostName(String createHostName) {
        this.createHostName = createHostName;
    }

    final public String getUpdateHostName() {
        return updateHostName;
    }

    final public void setUpdateHostName(String updateHostName) {
        this.updateHostName = updateHostName;
    }

    final public String getUpdateIp() {
        return updateIp;
    }

    final public void setUpdateIp(String updateIp) {
        this.updateIp = updateIp;
    }

    final public Date getGmtModify() {
        return gmtModify;
    }

    final public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    final public E getContent() {
        return content;
    }

    final public void setContent(E content) {
        this.content = content;
    }

    final public List<String> getSuccessExecuters() {
        return successExecuters;
    }

    final public void setSuccessExecuters(List<String> successExecuters) {
        this.successExecuters = successExecuters;
    }

    final public void addSuccessExecuter(String successExecuter){
        successExecuters.add(successExecuter);
    }
}