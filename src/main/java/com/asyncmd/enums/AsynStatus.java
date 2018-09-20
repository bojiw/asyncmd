/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.enums;

/**
 *
 * 异步命令状态枚举
 * @author wangwendi
 * @version $Id: AsynStatus.java, v 0.1 2018年09月20日 下午5:54 wangwendi Exp $
 */
public enum AsynStatus {

    INIT("INIT","初始化"),
    EXECUTE("EXECUTE","执行中"),
    SUCCESS("SUCCESS","成功"),
    ERROR("ERROR","失败");

    private String status;
    private String desc;


    AsynStatus(String status,String desc){
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