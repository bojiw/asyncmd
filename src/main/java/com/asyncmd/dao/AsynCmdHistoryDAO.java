
package com.asyncmd.dao;

import com.asyncmd.model.AsynCmdHistoryDO;

import java.util.List;

/**
 * 异步命令历史表
 * @author wangwendi
 * @version $Id: AsynCmdHistoryDAO.java, v 0.1 2019年07月11日 wangwendi Exp $
 */
public interface AsynCmdHistoryDAO {

    /**
     * 保存历史异步命令
     * @param asynCmdHistoryDO
     * @return
     */
    long saveCmd(AsynCmdHistoryDO asynCmdHistoryDO);

    /**
     * 保存历史异步命令
     * @param asynCmdHistoryDOs
     * @return
     */
    long batchSaveCmd(Integer tableIndex, List<AsynCmdHistoryDO> asynCmdHistoryDOs);


}