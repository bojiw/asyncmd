
package com.asyncmd.dao.impl;

import com.asyncmd.dao.AsynCmdHistoryDAO;
import com.asyncmd.model.AsynCmdHistoryDO;
import com.asyncmd.utils.JdbcTemplateUtil;
import com.asyncmd.utils.SubTableUtil;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wangwendi
 * @version $Id: AsynCmdHistoryDAOImpl.java, v 0.1 2019年07月22日 wangwendi Exp $
 */
@Service
public class AsynCmdHistoryDAOImpl implements AsynCmdHistoryDAO {

    @Override
    public long saveCmd(AsynCmdHistoryDO asynCmdHistoryDO) {
        String tableName = SubTableUtil.getTableName(null, asynCmdHistoryDO.getBizId(), AsynCmdHistoryDO.TABLE_NAME);

        String sql = "INSERT INTO " + tableName + " (cmd_type, content,biz_id, create_host_name, create_ip, create_name,execute_num,next_time,status,update_host_name,update_ip,success_executers,env,exception,gmt_create,gmt_modify) " +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return JdbcTemplateUtil.newInstance().update(sql,new Object[]{asynCmdHistoryDO.getCmdType(),asynCmdHistoryDO.getContent(),asynCmdHistoryDO.getBizId(),asynCmdHistoryDO.getCreateHostName(),
                asynCmdHistoryDO.getCreateIp(),asynCmdHistoryDO.getCreateName(),asynCmdHistoryDO.getExecuteNum(),asynCmdHistoryDO.getNextTime(),asynCmdHistoryDO.getStatus(),
                asynCmdHistoryDO.getUpdateHostName(),asynCmdHistoryDO.getUpdateIp(),asynCmdHistoryDO.getSuccessExecuters(),asynCmdHistoryDO.getEnv(),asynCmdHistoryDO.getException(),new java.util.Date(),new java.util.Date()});

    }

    @Override
    public long batchSaveCmd(Integer tableIndex,final List<AsynCmdHistoryDO> asynCmdHistoryDOs) {
        String tableName = SubTableUtil.getTableName(tableIndex, null, AsynCmdHistoryDO.TABLE_NAME);

        String sql = "INSERT INTO " + tableName + " (cmd_type, content,biz_id, create_host_name, create_ip, create_name,execute_num,next_time,status,update_host_name,update_ip,success_executers,env,exception,gmt_create,gmt_modify) " +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int[] ints = JdbcTemplateUtil.newInstance().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                AsynCmdHistoryDO asynCmdHistoryDO = asynCmdHistoryDOs.get(i);
                preparedStatement.setString(1, asynCmdHistoryDO.getCmdType());
                preparedStatement.setString(2, asynCmdHistoryDO.getContent());
                preparedStatement.setString(3, asynCmdHistoryDO.getBizId());
                preparedStatement.setString(4, asynCmdHistoryDO.getCreateHostName());

                preparedStatement.setString(5, asynCmdHistoryDO.getCreateIp());

                preparedStatement.setString(6, asynCmdHistoryDO.getCreateName());
                preparedStatement.setInt(7, asynCmdHistoryDO.getExecuteNum());
                Date nextTime = new Date(asynCmdHistoryDO.getNextTime().getTime());
                preparedStatement.setDate(8, nextTime);
                preparedStatement.setString(9, asynCmdHistoryDO.getStatus());
                preparedStatement.setString(10, asynCmdHistoryDO.getUpdateHostName());
                preparedStatement.setString(11, asynCmdHistoryDO.getUpdateIp());
                preparedStatement.setString(12, asynCmdHistoryDO.getSuccessExecuters());
                preparedStatement.setString(13,asynCmdHistoryDO.getEnv());
                preparedStatement.setString(14,asynCmdHistoryDO.getException());
                Date gmtCreate = new Date(System.currentTimeMillis());
                preparedStatement.setDate(15, gmtCreate);
                Date gmtModify = new Date(asynCmdHistoryDO.getGmtModify().getTime());
                preparedStatement.setDate(16, gmtModify);

            }

            @Override
            public int getBatchSize() {
                return asynCmdHistoryDOs.size();
            }
        });
        if (ints == null){
            return 0;
        }

        return ints.length;
    }
}