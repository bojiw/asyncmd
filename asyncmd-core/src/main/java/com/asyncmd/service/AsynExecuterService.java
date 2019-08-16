
package com.asyncmd.service;

import com.asyncmd.enums.AsynStatus;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynUpdateParam;
import com.asyncmd.model.Frequency;

import java.util.Date;
import java.util.List;

/**
 *
 * @author wangwendi
 * @version $Id: AsynExecuterService.java, v 0.1 2018年09月30日 wangwendi Exp $
 */
public interface AsynExecuterService {

    /**
     * 插入异步命令
     * @param asynCmd
     * @return
     */
    long saveCmd(AsynCmd asynCmd);

    /**
     * 备份异步命令
     * @param asynCmds
     */
    void backupCmd(final Integer tableIndex, final List<AsynCmd> asynCmds);

    /**
     * 获取异步命令
     * @return
     */
    List<AsynCmd> queryAsynCmd(Integer limit,int tableIndex,AsynStatus status,Date whereNextTime);


    boolean updateStatus(AsynUpdateParam param);

    /**
     * 根据业务id修改状态
     * @param param
     */
    boolean batchUpdateStatus(AsynUpdateParam param, Integer tableIndex);


    /**
     * 获取下一次执行时间
     * @param asynCmd
     * @return
     */
    Date getNextTime(AsynCmd asynCmd);

    /**
     * 根据bizId获取异步命令
     * @param bizId
     * @return
     */
    AsynCmd getAsynCmdByBizId(String bizId,Class<? extends AsynCmd>asynCmdClass);

    /**
     * 依赖异步命令执行成功
     * @param bizId
     * @return
     */
    Boolean relyAsynCmdSuccess(String bizId);


}