/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.dao;

import com.asyncmd.model.AsynCmdDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

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


    /**
     * 根据业务id集合批量更新状态
     * @param bizIds
     * @param status
     * @return
     */
    long batchUpdateStatus(@Param("bizIds") List<String> bizIds,@Param("status") String status);


    /**
     * 查询异步命令
     * @param limit
     * @param executerTime
     * @return
     */
    List<AsynCmdDO> queryAsynCmd(@Param("limit") int limit,@Param("executerTime") Date executerTime);



}