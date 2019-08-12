
package com.asyncmd.callback;

/**
 * 异常回调
 * @author wangwendi
 * @version $Id: ErrorCallBack.java, v 0.1 2019年07月31日 下午8:51 wangwendi Exp $
 */
public interface ErrorCallBack {



    /**
     * 异步命令执行异常回调
     * @param callBack
     */
    void callBack(CallBack callBack);
}