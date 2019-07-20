/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.exception;

/**
 *
 * @author wangwendi
 * @version $Id: AsynException.java, v 0.1 2018年09月30日 wangwendi Exp $
 */
public class AsynException extends RuntimeException {

    private Integer code;

    public AsynException(AsynExCode code){
        super(code.getMessage());
        this.code = code.getCode();
    }
    public AsynException(AsynExCode code,String message){
        super(message);
        this.code = code.getCode();
    }

    public AsynException(AsynExCode code,Throwable throwable){
        super(code.getMessage(),throwable);
        this.code = code.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}