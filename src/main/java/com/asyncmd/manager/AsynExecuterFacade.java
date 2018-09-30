/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.manager;

import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynExecuter;

/**
 *
 * 异步命令门面服务
 *
 * @author wangwendi
 * @version $Id: AsynExecuterFacade.java, v 0.1 2018年09月30日 wangwendi Exp $
 */
public interface AsynExecuterFacade {

    /**
     * 注册异步命令执行器
     *
     * @param asynExecuter
     */
    void registerAsynExecuter(AsynExecuter<? extends AsynCmd> asynExecuter);

    /**
     * 保存执行异步命令
     * @param asynCmd
     */
    void saveExecuterAsynCmd(AsynCmd asynCmd);
}