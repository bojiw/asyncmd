
package com.asyncmd.callback;

import com.asyncmd.model.AsynCmd;

/**
 * 异常回调
 * @author wangwendi
 * @version $Id: ErrorCallBack.java, v 0.1 2019年07月31日 下午8:51 wangwendi Exp $
 */
public interface ErrorCallBack {

    /**
     * 每次执行异常回调
     * @param asynCmd
     */
    void everyErrorCallBack(AsynCmd asynCmd,Exception e);


    /**
     * 异步命令状态为error时会回调
     * @param asynCmd
     */
    void errorCallBack(AsynCmd asynCmd,Exception e);
}