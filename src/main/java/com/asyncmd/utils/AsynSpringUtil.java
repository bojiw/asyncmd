
package com.asyncmd.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 *
 * @author wangwendi
 * @version $Id: SpringContextUtil.java, v 0.1 2019年01月15日 wangwendi Exp $
 */
@Component
public class AsynSpringUtil implements ApplicationContextAware {
    private static Log log = LogFactory.getLog(AsynSpringUtil.class);


    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
        log.info("ApplicationContext--------------");
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        if (null == applicationContext) {
            int i = 0;
            while (i++ < 30 && null == applicationContext) {
                try {
                    // 等待初始化
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                }
            }
            // 如果90s过去了还没初始化成功，则抛出异常
            if(null == applicationContext) {
                throw new IllegalStateException("asyn注入ApplicaitonContext异常,请联系开发人员");
            }
        }
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

}