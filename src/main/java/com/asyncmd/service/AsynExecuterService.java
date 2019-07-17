/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.service;

import com.asyncmd.enums.AsynStatus;
import com.asyncmd.model.AsynUpdateParam;
import com.asyncmd.utils.convert.AsynCmdConvert;
import com.asyncmd.dao.AsynCmdDAO;
import com.asyncmd.dao.AsynCmdHistoryDAO;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynCmdDO;
import com.asyncmd.utils.TransactionTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
     * @param asynCmd
     */
    void backupCmd(final AsynCmd asynCmd);

    /**
     * 获取异步命令
     * @return
     */
    List<AsynCmd> queryAsynCmd(int limit,int tableIndex,AsynStatus status);


    /**
     * 根据业务id修改状态
     * @param param
     */
    boolean batchUpdateStatus(AsynUpdateParam param, int tableIndex);


}