/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.utils;

import com.asyncmd.exception.AsynExCode;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 事务模版工具类
 * @author wangwendi
 * @version $Id: TransactionTemplateUtil.java, v 0.1 2019年07月12日 上午9:40 wangwendi Exp $
 */
public class TransactionTemplateUtil {
    private static final TransactionTemplateUtil instance = new TransactionTemplateUtil();

    private volatile TransactionTemplate template;

    public static TransactionTemplateUtil newInstance(){
        return instance;
    }


    public void setTransactionTemplate(TransactionTemplate template){
        ValidationUtil.isNull(template,AsynExCode.TEMPLATE_NULL);
        this.template = template;
    }

    public TransactionTemplate getTemplate() {
        ValidationUtil.isNull(template,AsynExCode.TEMPLATE_NULL);
        return template;
    }
}