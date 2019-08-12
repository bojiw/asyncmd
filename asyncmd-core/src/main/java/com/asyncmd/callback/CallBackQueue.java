package com.asyncmd.callback;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 回调队列
 * @author wangwendi
 * @date 2019/8/12
 */
public class CallBackQueue {

    private static LinkedBlockingQueue<CallBack> linkedBlockingQueue = new LinkedBlockingQueue<>(2000);


    /**
     * 入栈
     * @param callBack
     * @return 入栈成功返回true
     */
    public static boolean offer(CallBack callBack){
        return linkedBlockingQueue.offer(callBack);
    }

    /**
     * 出栈
     * @return
     */
    public static CallBack poll(){
        return linkedBlockingQueue.poll();
    }

    public static boolean isEmpty(){
        return linkedBlockingQueue.isEmpty();
    }


}