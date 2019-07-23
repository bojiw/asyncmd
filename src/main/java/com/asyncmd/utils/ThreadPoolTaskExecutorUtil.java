
package com.asyncmd.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wangwendi
 * @version $Id: ThreadPoolTaskExecutorUtil.java, v 0.1 2019年07月16日 wangwendi Exp $
 */
public class ThreadPoolTaskExecutorUtil {

    private static final ThreadPoolTaskExecutorUtil instance = new ThreadPoolTaskExecutorUtil();

    /**
     * 线程池
     */
    private ThreadPoolTaskExecutor poolTaskExecutor;


    public ThreadPoolTaskExecutorUtil(){
        //默认线程池
        poolTaskExecutor = new ThreadPoolTaskExecutor();
        //线程池名称
        poolTaskExecutor.setThreadNamePrefix("asyn-thread");
        //核心线程数量为5
        poolTaskExecutor.setCorePoolSize(5);
        //最大线程数量为40
        poolTaskExecutor.setMaxPoolSize(100);
        //空闲线程等待时间为300秒
        poolTaskExecutor.setKeepAliveSeconds(300);
        //等待队列为100
        poolTaskExecutor.setQueueCapacity(50);
        //如果线程池满 则抛异常
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    }
    public void init(){
        poolTaskExecutor.initialize();
    }

    public static ThreadPoolTaskExecutorUtil newInstance(){
        return instance;
    }

    public void setCorePoolSize(int corePoolSize){
        poolTaskExecutor.setCorePoolSize(corePoolSize);
    }
    public void setMaxPoolSize(int maxPoolSize){
        poolTaskExecutor.setMaxPoolSize(maxPoolSize);
    }
    public void setQueueCapacity(int queueCapacity){
        poolTaskExecutor.setQueueCapacity(queueCapacity);
    }

    public ThreadPoolTaskExecutor getPoolTaskExecutor() {
        return poolTaskExecutor;
    }



}