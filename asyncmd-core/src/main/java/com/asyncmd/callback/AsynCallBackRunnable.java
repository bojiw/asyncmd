package com.asyncmd.callback;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Objects;

/**
 * @author wangwendi
 * @date 2019/8/8
 */
public class AsynCallBackRunnable implements Runnable{

    private static Log log = LogFactory.getLog(AsynCallBackRunnable.class);

    private AbstractErrorCallBack abstractErrorCallBack;

    public AsynCallBackRunnable(AbstractErrorCallBack abstractErrorCallBack){
        this.abstractErrorCallBack = abstractErrorCallBack;
    }

    @Override
    public void run() {
        if (abstractErrorCallBack == null){
            return;
        }
        while (true){
            callBack();
            try {
                Thread.sleep(1000L);
            }catch (Exception e){
                log.error("回调接口线程等待异常",e);
            }
        }

    }

    private void callBack(){
        while (!CallBackQueue.isEmpty()){
            CallBack callBack = CallBackQueue.take();
            try {
                if (callBack == null){
                    return;
                }
                abstractErrorCallBack.callBack(callBack);

            }catch (Exception ex){
                log.error("调用异常回调接口异常,bizId=" + callBack.getAsynCmd().getBizId(),ex);
            }

        }
    }


}