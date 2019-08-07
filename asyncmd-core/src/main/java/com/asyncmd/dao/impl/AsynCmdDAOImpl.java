
package com.asyncmd.dao.impl;

import com.asyncmd.dao.AsynCmdDAO;
import com.asyncmd.dao.mapper.AsynCmdRowMapper;
import com.asyncmd.model.AsynCmdDO;
import com.asyncmd.model.AsynQueryParam;
import com.asyncmd.model.AsynUpdateParam;
import com.asyncmd.utils.JdbcTemplateUtil;
import com.asyncmd.utils.SubTableUtil;
import com.google.common.collect.Lists;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author wangwendi
 * @version $Id: AsynCmdDAOImpl.java, v 0.1 2019年07月22日 wangwendi Exp $
 */
@Service
public class AsynCmdDAOImpl implements AsynCmdDAO {


    @Override
    public long saveCmd(AsynCmdDO asynCmdDO) {
        String tableName = SubTableUtil.getTableName(null, asynCmdDO.getBizId(), AsynCmdDO.TABLE_NAME);

        String sql = "INSERT INTO " + tableName + " (cmd_type, content,biz_id, create_host_name, create_ip, create_name,execute_num,next_time,status,update_host_name,update_ip,success_executers,env,exception,gmt_create,gmt_modify) " +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return JdbcTemplateUtil.newInstance().update(sql,new Object[]{asynCmdDO.getCmdType(),asynCmdDO.getContent(),asynCmdDO.getBizId(),asynCmdDO.getCreateHostName(),
                asynCmdDO.getCreateIp(),asynCmdDO.getCreateName(),asynCmdDO.getExecuteNum(),asynCmdDO.getNextTime(),asynCmdDO.getStatus(),
                asynCmdDO.getUpdateHostName(),asynCmdDO.getUpdateIp(),asynCmdDO.getSuccessExecuters(),asynCmdDO.getEnv(),asynCmdDO.getException(),new Date(),new Date()});
    }
    @Override
    public long delCmd(String bizId) {
        String tableName = SubTableUtil.getTableName(null, bizId, AsynCmdDO.TABLE_NAME);
        String sql = "DELETE FROM " + tableName + " WHERE biz_id=?";
        return JdbcTemplateUtil.newInstance().update(sql,new Object[]{bizId});
    }
    @Override
    public long batchDelCmd(Integer tableIndex, final List<String> bizIds) {
        String tableName = SubTableUtil.getTableName(tableIndex, null, AsynCmdDO.TABLE_NAME);
        String sql = "DELETE FROM " + tableName + " WHERE biz_id=?";
        int[] ints = JdbcTemplateUtil.newInstance().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, bizIds.get(i));
            }
            @Override
            public int getBatchSize() {
                return bizIds.size();
            }
        });
        if (ints == null){
            return 0;
        }
        return ints.length;
    }
    @Override
    public long updateStatus(AsynUpdateParam asynUpdateParam) {
        String tableName = SubTableUtil.getTableName(null, asynUpdateParam.getBizId(), AsynCmdDO.TABLE_NAME);
        List<Object> param = Lists.newArrayList();
        String sqlBuildParam = getSqlBuildParam(tableName, param, asynUpdateParam);
        param.add(asynUpdateParam.getBizId());
        Object[] objects = new Object[param.size()];
        for (int i=0;i < param.size();i++){
            objects[i] = param.get(i);
        }
        return JdbcTemplateUtil.newInstance().update(sqlBuildParam,objects);
    }

    /**
     * 获取sql并且填充请求对象
     * @param tableName
     * @param param
     * @param asynUpdateParam
     * @return
     */

    private String getSqlBuildParam(String tableName,List<Object> param,AsynUpdateParam asynUpdateParam){
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ").append(tableName).append(" set status = ?");
        param.add(asynUpdateParam.getStatus());
        sql.append(" ,gmt_modify=?");
        param.add(new Date());
        if (asynUpdateParam.getExecuter() != null && asynUpdateParam.getExecuter()){
            sql.append(" ,execute_num = execute_num + 1");
        }
        if (asynUpdateParam.getReset() != null && asynUpdateParam.getReset()){
            sql.append(" ,execute_num = execute_num + 1");
        }
        if (asynUpdateParam.getSuccessExecutes() != null && asynUpdateParam.getSuccessExecutes().length() > 0){
            sql.append(",success_executers = ?");
            param.add(asynUpdateParam.getSuccessExecutes());
        }
        if (asynUpdateParam.getUpdateHostName() != null && asynUpdateParam.getUpdateHostName().length() > 0){
            sql.append(",update_host_name = ?");
            param.add(asynUpdateParam.getUpdateHostName());
        }
        if (asynUpdateParam.getUpdateIp() != null && asynUpdateParam.getUpdateIp().length() > 0){
            sql.append(",update_ip = ?");
            param.add(asynUpdateParam.getUpdateIp());
        }
        if (asynUpdateParam.getNextTime() != null){
            sql.append(",next_time = ?");
            param.add(asynUpdateParam.getNextTime());
        }
        if (asynUpdateParam.getException() != null){
            sql.append(",exception = ?");
            param.add(asynUpdateParam.getException());
        }
        sql.append(" where status = ? and biz_id = ?  ");
        param.add(asynUpdateParam.getWhereAsynStatus());
        return sql.toString();
    }
    @Override
    public long batchUpdateStatus(Integer tableIndex,final AsynUpdateParam asynUpdateParam) {
        String tableName = SubTableUtil.getTableName(tableIndex,null, AsynCmdDO.TABLE_NAME);
        final List<Object> param = Lists.newArrayList();
        String sql = getSqlBuildParam(tableName, param, asynUpdateParam);
        int[] ints = JdbcTemplateUtil.newInstance().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                String bizId = asynUpdateParam.getBizIds().get(i);
                for (int j = 0;j < param.size();j++) {
                    preparedStatement.setObject(j + 1, param.get(j));
                }
                preparedStatement.setString(param.size() + 1, bizId);
            }
            @Override
            public int getBatchSize() {
                return asynUpdateParam.getBizIds().size();
            }
        });
        if (ints == null){
            return 0;
        }
        return ints.length;
    }

    @Override
    public List<AsynCmdDO> queryAsynCmd(Integer tableIndex, AsynQueryParam param) {
        String tableName = SubTableUtil.getTableName(tableIndex,null, AsynCmdDO.TABLE_NAME);
        StringBuffer sql = getSelectBase(tableName);
        sql.append(" where status = ? and next_time <= ? and env = ?");
        if (param.getDesc() != null){
            sql.append(" order by gmt_create");
        }
        if (param.getLimit() != null){
            sql.append(" limit ?");
            return JdbcTemplateUtil.newInstance().query(sql.toString(),
                    new Object[]{param.getStatus(),param.getExecuterTime(),param.getEnv(),param.getLimit()},new AsynCmdRowMapper());
        }

        return JdbcTemplateUtil.newInstance().query(sql.toString(),
                new Object[]{param.getStatus(),param.getExecuterTime(),param.getEnv()},new AsynCmdRowMapper());
    }

    private StringBuffer getSelectBase(String tableName){
        StringBuffer sql = new StringBuffer();
        sql.append("select cmd_id,cmd_type,content,biz_id,create_host_name,create_ip,create_name,")
                .append("execute_num,gmt_create,gmt_modify,next_time,status,update_host_name,update_ip,")
                .append("success_executers,env,exception from ").append(tableName);
        return sql;
    }
    @Override
    public AsynCmdDO getAsynCmdByBizId(String bizId) {
        String tableName = SubTableUtil.getTableName(null,bizId, AsynCmdDO.TABLE_NAME);
        StringBuffer sql = getSelectBase(tableName);
        sql.append("where biz_id=?");
        return (AsynCmdDO)JdbcTemplateUtil.newInstance().queryForObject(sql.toString(),new Object[]{bizId},new AsynCmdRowMapper());
    }
}