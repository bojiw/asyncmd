
package com.asyncmd.model;

import java.util.Date;
import java.util.List;

/**
 * 异步命令更新请求参数
 * @author wangwendi
 * @version $Id: AsynUpdateParam.java, v 0.1 2019年07月17日 wangwendi Exp $
 */
public class AsynUpdateParam {

    private String bizId;
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

    /**
     * 下一次执行时间
     */
    private Date nextTime;

    /**
     * 执行成功的执行器
     */
    private String successExecutes;

    /**
     * 执行更新操作的服务器名称
     */
    private String updateHostName;

    /**
     * 执行更新操作的服务器ip
     */
    private String updateIp;

    /**
     * 异常信息
     */
    private String exception;


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

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public String getSuccessExecutes() {
        return successExecutes;
    }

    public void setSuccessExecutes(String successExecutes) {
        this.successExecutes = successExecutes;
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

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}