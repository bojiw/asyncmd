/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.config;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangwendi
 * @version $Id: InitConfig.java, v 0.1 2019年07月20日 下午11:16 wangwendi Exp $
 */
public class InitConfig implements SmartInitializingSingleton {
    @Autowired
    private AsynGroupConfig asynGroupConfig;

    /**
     * 异步命令注册在InitializingBean.afterPropertiesSet 需要在注册以后在进行初始化 防止定时任务执行时还未注册
     * 所以采用Spring的SmartInitializingSingleton 这个是在所有单例类初始化以后会执行的回调 来保证所有的异步命令执行器都准备就绪以后再执行
     */
    public void afterSingletonsInstantiated() {
        asynGroupConfig.init();
    }

}