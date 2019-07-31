/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.model;

import com.asyncmd.enums.DispatchMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异步命令对象配置注解
 * @author wangwendi
 * @version $Id: AsynCmdConf.java, v 0.1 2019年07月31日 下午8:06 wangwendi Exp $
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AsynCmdConf {

    /**
     * 调度方式 如果某些异步命令需要修改默认的调度方式 可以设置这个
     * @return
     */
    DispatchMode dispatchMode() default DispatchMode.ASYN;

    /**
     * 调度频率
     * 如果有特别的异步命令不想要用全局重试频率 可以设置
     * 重试执行频率 5s,10s,1m,1h
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次1分钟以后执行 第四次1小时以后执行 之后都是间隔1小时执行
     * 执行频率 5s,10s,20s
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次20秒以后执行
     * @return
     */
    String executerFrequency() default "";
}