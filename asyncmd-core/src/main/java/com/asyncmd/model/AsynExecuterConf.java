/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异步命令执行器配置注解
 * @author wangwendi
 * @version $Id: AsynExecuterConf.java, v 0.1 2019年07月31日 下午8:37 wangwendi Exp $
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AsynExecuterConf {

    int sort() default 100;

}