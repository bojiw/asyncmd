package com.asyncmd.callback;

import com.asyncmd.enums.AsynStatus;
import com.asyncmd.model.AsynCmd;

/**
 * @author wangwendi
 * @date 2019/8/8
 */
public class CallBack {
    /**
     * 执行时的异步命令数据
     */
    private AsynCmd asynCmd;
    /**
     * 异常报错
     */
    private Exception exception;
    /**
     * 异步命令最新状态
     */
    private AsynStatus asynStatus;

    public AsynCmd getAsynCmd() {
        return asynCmd;
    }

    public void setAsynCmd(AsynCmd asynCmd) {
        this.asynCmd = asynCmd;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public AsynStatus getAsynStatus() {
        return asynStatus;
    }

    public void setAsynStatus(AsynStatus asynStatus) {
        this.asynStatus = asynStatus;
    }
}