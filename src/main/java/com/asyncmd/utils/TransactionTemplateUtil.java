
package com.asyncmd.utils;

import com.asyncmd.exception.AsynExCode;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * 事务模版工具类
 * @author wangwendi
 * @version $Id: TransactionTemplateUtil.java, v 0.1 2019年07月12日 wangwendi Exp $
 */
public class TransactionTemplateUtil {
    private static final TransactionTemplateUtil instance = new TransactionTemplateUtil();

    private TransactionTemplate template = new TransactionTemplate();

    public static TransactionTemplateUtil newInstance(){
        return instance;
    }

    public TransactionTemplate getTemplate() {
        ValidationUtil.isNull(template,AsynExCode.TEMPLATE_NULL);
        return template;
    }
    public void setDataSource(DataSource dataSource){
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        template.setTransactionManager(transactionManager);
    }
}