/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.exception;

/**
 *
 * 异常code
 * @author wangwendi
 * @version $Id: AsynExCode.java, v 0.1 2018年09月30日 上午11:39 wangwendi Exp $
 */
public enum AsynExCode {

    SYS_ERROR(1000,"系统异常");

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