/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.manager;

/**
 * 异步命令任务管理器
 * @author wangwendi
 * @version $Id: AsynExecuterJobManager.java, v 0.1 2019年07月16日 下午7:21 wangwendi Exp $
 */
public interface AsynExecuterJobManager {

    /**
     * 调度异步命令执行
     * @param tableIndex
     */
    void executer(int tableIndex);

}