
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

    public static final String TABLE_NAME = "asyn_cmd";
    /**
     * 唯一id
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
     * 执行成功的处理器名
     */
    private String successExecuters;

    /**
     * 所在环境 预发和正式环境需要不同 对正式环境和预发环境做隔离 本地环境和开发环境也可以做隔离
     */
    private String env;
    /**
     * 异常信息
     */
    private String exception;
    /**
     * 依赖的业务id
     */
    private String relyBizId;


    public AsynCmdDO(){

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

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
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

    public String getCreateHostName() {
        return createHostName;
    }

    public void setCreateHostName(String createHostName) {
        this.createHostName = createHostName;
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

    public String getUpdateHostName() {
        return updateHostName;
    }

    public void setUpdateHostName(String updateHostName) {
        this.updateHostName = updateHostName;
    }

    public String getUpdateIp() {
        return updateIp;
    }

    public void setUpdateIp(String updateIp) {
        this.updateIp = updateIp;
    }

    public String getSuccessExecuters() {
        return successExecuters;
    }

    public void setSuccessExecuters(String successExecuters) {
        this.successExecuters = successExecuters;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getRelyBizId() {
        return relyBizId;
    }

    public void setRelyBizId(String relyBizId) {
        this.relyBizId = relyBizId;
    }
}