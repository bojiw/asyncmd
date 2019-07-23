
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
        poolTaskExecutor.setMaxPoolSize(40);
        //空闲线程等待时间为300秒
        poolTaskExecutor.setKeepAliveSeconds(300);
        //等待队列为100
        poolTaskExecutor.setQueueCapacity(100);
        //如果线程池满 则抛异常
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        poolTaskExecutor.initialize();
    }

    public static ThreadPoolTaskExecutorUtil newInstance(){
        return instance;
    }

    public void setPoolTaskExecutor(ThreadPoolTaskExecutor poolTaskExecutor) {
        //如果有线程运行 等运行完再关闭 正常情况执行这里时没有线程会运行 只是做个预防
        this.poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        this.poolTaskExecutor.shutdown();
        this.poolTaskExecutor = poolTaskExecutor;
    }

    public ThreadPoolTaskExecutor getPoolTaskExecutor() {
        return poolTaskExecutor;
    }



}