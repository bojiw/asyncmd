/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.exception;

/**
 *
 * 异常code
 * @author wangwendi
 * @version $Id: AsynExCode.java, v 0.1 2018年09月30日 wangwendi Exp $
 */
public enum AsynExCode {

    SYS_ERROR(1000,"系统异常"),
    ILLEGAL(1001,"参数异常"),
    THREAD_POLL_ERROR(1002,"线程池异常,预计线程池已满"),
    TEMPLATE_NULL(2001,"事务模板方法为空"),
    EXECUTER_FREQUENCY_ILLEGAL(2002,"executerFrequency调度频率参数异常,标准格式为:5s,10s,1m,2h"),
    CRON_ILLEGAL(2002,"cron格式异常,请检测是否为正确的cron表达式"),
    ;

    private Integer code;
    private String message;
    AsynExCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}