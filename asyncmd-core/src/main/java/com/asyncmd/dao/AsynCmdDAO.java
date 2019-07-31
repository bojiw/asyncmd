
package com.asyncmd.dao;

import com.asyncmd.model.AsynCmdDO;
import com.asyncmd.model.AsynQueryParam;
import com.asyncmd.model.AsynUpdateParam;

import java.util.Date;
import java.util.List;

/**
 *
 * 异步命令DAO
 *
 * @author wangwendi
 * @version $Id: AsynCmdDAO.java, v 0.1 2018年09月30日  wangwendi Exp $
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
     * 批量删除异步命令
     * @param bizIds
     * @return
     */
    long batchDelCmd(Integer tableIndex,List<String> bizIds);



    /**
     * 分表根据业务id集合更新状态
     * @param asynUpdateParam
     * @return
     */
    long updateStatus(AsynUpdateParam asynUpdateParam);


    /**
     * 分表根据业务id集合批量更新状态
     * @param asynUpdateParam
     * @return
     */
    long batchUpdateStatus(Integer tableIndex,AsynUpdateParam asynUpdateParam);



    /**
     * 查询分表异步命令
     * @param tableIndex
     * @param param
     * @return
     */
    List<AsynCmdDO> queryAsynCmd(Integer tableIndex, AsynQueryParam param);



    /**
     * 分表根据bizId获取异步命令对象
     * @param bizId
     * @return
     */
    AsynCmdDO getAsynCmdByBizId(String bizId);

}