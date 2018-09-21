/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.enums;

/**
 *
 * 调度模式
 * @author wangwendi
 * @version $Id: DispatchMode.java, v 0.1 2018年09月21日 上午11:53 wangwendi Exp $
 */
public enum DispatchMode {

    DEFAULT("DEFAULT","调度中心调度"),
    ASYN("ASYN","异步执行"),
    ASY("ASY","同步执行");



    private String status;
    private String desc;


    DispatchMode(String status,String desc){
        this.status = status;
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}