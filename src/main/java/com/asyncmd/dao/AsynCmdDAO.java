/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.dao;

import com.asyncmd.model.AsynCmdDO;

/**
 *
 * 异步命令DAO
 *
 * @author wangwendi
 * @version $Id: AsynCmdDAO.java, v 0.1 2018年09月30日 下午4:31 wangwendi Exp $
 */
public interface AsynCmdDAO {

    /**
     * 保存异步命令
     * @param asynCmdDO
     * @return
     */
    long saveCmd(AsynCmdDO asynCmdDO);

    /**
     * 删除异步命令
     * @param bizId
     * @return
     */
    long delCmd(String bizId);


}