
package com.asyncmd.utils;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author wangwendi
 * @version $Id: JdbcTemplateUtil.java, v 0.1 2019年07月22日 wangwendi Exp $
 */
public class JdbcTemplateUtil {
    private static final JdbcTemplateUtil instance = new JdbcTemplateUtil();

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public static JdbcTemplateUtil newInstance(){
        return instance;
    }

    public int update(String sql,Object[] objects){
        return jdbcTemplate.update(sql,objects);
    }
    public List query(String sql, Object[] objects, RowMapper rowMapper){
        return jdbcTemplate.query(sql,objects,rowMapper);
    }

    public Object queryForObject(String sql, Object[] objects, RowMapper rowMapper){
        return jdbcTemplate.queryForObject(sql,objects,rowMapper);
    }
    public int[] batchUpdate(String sql,BatchPreparedStatementSetter pss){
        return jdbcTemplate.batchUpdate(sql,pss);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setDataSource(DataSource dataSource){
        jdbcTemplate.setDataSource(dataSource);
        jdbcTemplate.afterPropertiesSet();
    }
}