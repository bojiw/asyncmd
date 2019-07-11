/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.convert;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynCmdDO;

/**
 * @author wangwendi
 * @version $Id: AsynCmdConvert.java, v 0.1 2019年07月09日 下午9:02 wangwendi Exp $
 */
public class AsynCmdConvert {

    public static AsynCmdDO toDo(AsynCmd asynCmd){
        AsynCmdDO asynCmdDO = new AsynCmdDO();
        asynCmdDO.setCmdId(asynCmd.getCmdId());
        asynCmdDO.setCmdType(asynCmd.getCmdType());
        asynCmdDO.setBizId(asynCmd.getBizId());
        asynCmdDO.setContent(asynCmd.objectToJson(asynCmd.getContent()));
        asynCmdDO.setGmtCreate(asynCmd.getGmtCreate());
        asynCmdDO.setGmtModify(asynCmd.getGmtModify());
        asynCmdDO.setExecuteNum(asynCmd.getExecuteNum());
        asynCmdDO.setNextTime(asynCmd.getNextTime());
        asynCmdDO.setStatus(asynCmd.getStatus());
        asynCmdDO.setCreateHostname(asynCmd.getCreateHostname());
        asynCmdDO.setCreateIp(asynCmd.getCreateIp());
        asynCmdDO.setCreateName(asynCmd.getCreateName());
        asynCmdDO.setUpdateHostname(asynCmd.getUpdateHostname());
        asynCmdDO.setUpdateIp(asynCmd.getUpdateIp());
        return asynCmdDO;
    }
    public static AsynCmd toCmd(AsynCmdDO asynCmdDO,Class<AsynCmd> asynCmdClass){
        try {
            AsynCmd asynCmd = asynCmdClass.newInstance();
            asynCmd.setCmdId(asynCmdDO.getCmdId());
            asynCmd.setCmdType(asynCmdDO.getCmdType());
            asynCmd.setBizId(asynCmdDO.getBizId());
            asynCmd.setGmtCreate(asynCmdDO.getGmtCreate());
            asynCmd.setExecuteNum(asynCmdDO.getExecuteNum());
            asynCmd.setNextTime(asynCmdDO.getNextTime());
            asynCmd.setStatus(asynCmdDO.getStatus());
            asynCmd.setCreateHostname(asynCmdDO.getCreateHostname());
            asynCmd.setCreateIp(asynCmdDO.getCreateIp());
            asynCmd.setCreateName(asynCmdDO.getCreateName());
            asynCmd.setUpdateHostname(asynCmdDO.getUpdateHostname());
            asynCmd.setUpdateIp(asynCmdDO.getUpdateIp());
            asynCmd.setGmtModify(asynCmdDO.getGmtModify());
            asynCmd.setContent(asynCmd.jsonToObject(asynCmdDO.getContent()));
            return asynCmd;
        }catch (Exception e){
            throw new AsynException(AsynExCode.ILLEGAL);
        }
    }

}