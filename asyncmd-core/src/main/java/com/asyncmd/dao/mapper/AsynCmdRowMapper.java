
package com.asyncmd.dao.mapper;

import com.asyncmd.model.AsynCmdDO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author wangwendi
 * @version $Id: AsynCmdRowMapper.java, v 0.1 2019年07月22日 下午8:46 wangwendi Exp $
 */
public class AsynCmdRowMapper implements RowMapper<AsynCmdDO> {

    @Override
    public AsynCmdDO mapRow(ResultSet resultSet, int i) throws SQLException {
        AsynCmdDO asynCmdDO = new AsynCmdDO();
        asynCmdDO.setCmdId(resultSet.getLong("cmd_id"));
        asynCmdDO.setUpdateHostName(resultSet.getString("update_host_name"));
        asynCmdDO.setCreateHostName(resultSet.getString("create_host_name"));
        asynCmdDO.setSuccessExecuters(resultSet.getString("success_executers"));
        asynCmdDO.setGmtModify(resultSet.getTimestamp("gmt_modify"));
        asynCmdDO.setUpdateIp(resultSet.getString("update_ip"));
        asynCmdDO.setStatus(resultSet.getString("status"));
        asynCmdDO.setNextTime(resultSet.getTimestamp("next_time"));
        asynCmdDO.setGmtCreate(resultSet.getTimestamp("gmt_create"));
        asynCmdDO.setExecuteNum(resultSet.getInt("execute_num"));
        asynCmdDO.setCreateName(resultSet.getString("create_name"));
        asynCmdDO.setCreateIp(resultSet.getString("create_ip"));
        asynCmdDO.setContent(resultSet.getString("content"));
        asynCmdDO.setCmdType(resultSet.getString("cmd_type"));
        asynCmdDO.setBizId(resultSet.getString("biz_id"));
        asynCmdDO.setEnv(resultSet.getString("env"));
        return asynCmdDO;
    }
}