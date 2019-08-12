package com.asyncmd.callback;

import com.asyncmd.enums.AsynStatus;
import com.asyncmd.model.AsynCmd;

/**
 * 异步命令异常回调类
 * @author wangwendi
 * @date 2019/8/12
 */
public abstract class AbstractErrorCallBack implements ErrorCallBack {

    @Override
    public void callBack(CallBack callBack) {
        AsynCmd asynCmd = callBack.getAsynCmd();
        Exception exception = callBack.getException();
        AsynStatus asynStatus = callBack.getAsynStatus();
        this.everyErrorCallBack(asynCmd,exception);
        if (asynStatus == AsynStatus.ERROR){
            this.errorCallBack(asynCmd,exception);
        }
    }

    /**
     * 异步命令状态为error时会回调
     * @param asynCmd
     * @param e
     */
    protected void errorCallBack(AsynCmd asynCmd, Exception e){

    }

    /**
     * 异步命令状态为error时会回调
     * @param asynCmd
     */
    protected void everyErrorCallBack(AsynCmd asynCmd,Exception e){

    }
}