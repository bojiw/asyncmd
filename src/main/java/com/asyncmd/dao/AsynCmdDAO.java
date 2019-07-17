/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.dao;

import com.asyncmd.model.AsynCmdDO;
import com.asyncmd.model.AsynUpdateParam;
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
     * 分表保存异步命令
     * @param asynCmdDO
     * @return
     */
    long saveCmdSubTable(String tableIndex,AsynCmdDO asynCmdDO);


    /**
     * 删除异步命令
     * @param bizId
     * @return
     */
    long delCmd(String bizId);


    /**
     * 删除异步命令
     * @param bizId
     * @return
     */
    long delCmdSubTable(String tableIndex,String bizId);


    /**
     * 根据业务id集合批量更新状态
     * @param asynUpdateParam
     * @return
     */
    long batchUpdateStatus(AsynUpdateParam asynUpdateParam);

    /**
     * 分表根据业务id集合批量更新状态
     * @param asynUpdateParam
     * @return
     */
    long batchupdateStatusSubTable(String tableIndex,AsynUpdateParam asynUpdateParam);



    /**
     * 查询异步命令
     * @param limit
     * @param executerTime
     * @return
     */
    List<AsynCmdDO> queryAsynCmd(@Param("status") String status,
                                 @Param("limit") int limit,@Param("executerTime") Date executerTime);

    /**
     * 查询分表异步命令
     * @param limit
     * @param executerTime
     * @return
     */
    List<AsynCmdDO> querySubTableAsynCmd(@Param("tableIndex")String tableIndex, @Param("status") String status,
                                          @Param("limit") int limit,@Param("executerTime") Date executerTime);


}