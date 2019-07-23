
package com.asyncmd.dao.impl;

import com.asyncmd.dao.AsynCmdHistoryDAO;
import com.asyncmd.model.AsynCmdHistoryDO;
import com.asyncmd.utils.JdbcTemplateUtil;
import com.asyncmd.utils.SubTableUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author wangwendi
 * @version $Id: AsynCmdHistoryDAOImpl.java, v 0.1 2019年07月22日 wangwendi Exp $
 */
@Service
public class AsynCmdHistoryDAOImpl implements AsynCmdHistoryDAO {
    public long saveCmd(AsynCmdHistoryDO asynCmdHistoryDO) {
        String tableName = SubTableUtil.getTableName(null, asynCmdHistoryDO.getBizId(), AsynCmdHistoryDO.TABLE_NAME);

        String sql = "INSERT INTO " + tableName + " (cmd_type, content,biz_id, create_host_name, create_ip, create_name,execute_num,next_time,status,update_host_name,update_ip,success_executers,gmt_create,gmt_modify) " +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return JdbcTemplateUtil.newInstance().update(sql,new Object[]{asynCmdHistoryDO.getCmdType(),asynCmdHistoryDO.getContent(),asynCmdHistoryDO.getBizId(),asynCmdHistoryDO.getCreateHostName(),
                asynCmdHistoryDO.getCreateIp(),asynCmdHistoryDO.getCreateName(),asynCmdHistoryDO.getExecuteNum(),asynCmdHistoryDO.getNextTime(),asynCmdHistoryDO.getStatus(),
                asynCmdHistoryDO.getUpdateHostName(),asynCmdHistoryDO.getUpdateIp(),asynCmdHistoryDO.getSuccessExecuters(),new Date(),new Date()});

    }
}