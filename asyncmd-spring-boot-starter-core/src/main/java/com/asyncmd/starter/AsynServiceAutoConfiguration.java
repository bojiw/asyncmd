package com.asyncmd.starter;

import com.asyncmd.callback.AbstractErrorCallBack;
import com.asyncmd.config.AsynGroupConfig;
import com.asyncmd.starter.properties.AsynBackupJobProperties;
import com.asyncmd.starter.properties.AsynCoreProperties;
import com.asyncmd.starter.properties.AsynExecuterJobProperties;
import com.asyncmd.starter.properties.AsynPollProperties;
import com.asyncmd.starter.properties.AsynRestjobProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author wangwendi
 * @date 2019/8/12
 */
@Configuration
@EnableConfigurationProperties({AsynCoreProperties.class,AsynExecuterJobProperties.class
,AsynRestjobProperties.class,AsynBackupJobProperties.class,AsynPollProperties.class})

@ConditionalOnClass(AsynGroupConfig.class)
@ConditionalOnProperty(prefix = "asyn", value = "enabled", matchIfMissing = true)
@ImportResource(locations= {"classpath*:META-INF/asyn/applicationContext.xml"})
public class AsynServiceAutoConfiguration {

    @Autowired
    private AsynCoreProperties asynCoreProperties;

    @Autowired
    private AsynExecuterJobProperties asynExecuterJobProperties;

    @Autowired
    private AsynRestjobProperties asynRestjobProperties;


    @Autowired
    private AsynBackupJobProperties asynBackupJobProperties;

    @Autowired
    private AsynPollProperties asynPollProperties;

    @Resource(name = "groupErrorCallBack")
    private AbstractErrorCallBack abstractErrorCallBack;

    @Bean
    @ConditionalOnMissingBean(AsynGroupConfig.class)
    public AsynGroupConfig helloServiceConfiguration(DataSource dataSource) {
        AsynGroupConfig asynGroupConfig = new AsynGroupConfig();
        ParamUtil.assertStringNull(asynCoreProperties.getJobName(),"异步命令组件jobName不能为空");
        asynGroupConfig.setJobName(asynCoreProperties.getJobName());
        ParamUtil.assertStringNull(asynCoreProperties.getEnv(),"异步命令组件env不能为空");
        asynGroupConfig.setEnv(asynCoreProperties.getEnv());
        ParamUtil.assertStringNull(asynCoreProperties.getZookeeperUrl(),"异步命令组件zookeeper不能为空");
        asynGroupConfig.setZookeeperUrl(asynCoreProperties.getZookeeperUrl());
        asynGroupConfig.setDataSource(dataSource);
        if (ParamUtil.isNotNull(asynCoreProperties.getTableNum())){
            asynGroupConfig.setTableNum(asynCoreProperties.getTableNum());
        }
        if (ParamUtil.isNotEmpty(asynCoreProperties.getExecuterFrequencys())){
            asynGroupConfig.setExecuterFrequencys(asynCoreProperties.getExecuterFrequencys());
        }
        if (ParamUtil.isNotEmpty(asynExecuterJobProperties.getCron())){
            asynGroupConfig.setCron(asynExecuterJobProperties.getCron());
        }
        if (ParamUtil.isNotNull(asynExecuterJobProperties.getLimit())){
            asynGroupConfig.setLimit(asynExecuterJobProperties.getLimit());
        }
        if (ParamUtil.isNotNull(asynExecuterJobProperties.getDesc())){
            asynGroupConfig.setDesc(asynExecuterJobProperties.getDesc());
        }
        if (ParamUtil.isNotNull(asynExecuterJobProperties.getRetryNum())){
            asynGroupConfig.setRetryNum(asynExecuterJobProperties.getRetryNum());
        }
        if (ParamUtil.isNotNull(asynPollProperties.getCorePoolSize())){
            asynGroupConfig.setCorePoolSize(asynPollProperties.getCorePoolSize());
        }
        if (ParamUtil.isNotNull(asynPollProperties.getMaxPoolSize())){
            asynGroupConfig.setMaxPoolSize(asynPollProperties.getMaxPoolSize());
        }
        if (ParamUtil.isNotNull(asynPollProperties.getQueueCapacity())){
            asynGroupConfig.setQueueCapacity(asynPollProperties.getQueueCapacity());
        }
        if (ParamUtil.isNotEmpty(asynRestjobProperties.getRestCron())){
            asynGroupConfig.setRestCron(asynRestjobProperties.getRestCron());
        }
        if (ParamUtil.isNotNull(asynBackupJobProperties.getBackup())){
            asynGroupConfig.setBackup(asynBackupJobProperties.getBackup());
        }

        if (ParamUtil.isNotEmpty(asynBackupJobProperties.getBackupCron())){
            asynGroupConfig.setBackupCron(asynBackupJobProperties.getBackupCron());
        }
        if (ParamUtil.isNotNull(asynBackupJobProperties.getBeforeDate())){
            asynGroupConfig.setBeforeDate(asynBackupJobProperties.getBeforeDate());
        }
        if (ParamUtil.isNotNull(asynBackupJobProperties.getMaxNo())){
            asynGroupConfig.setMaxNo(asynBackupJobProperties.getMaxNo());
        }
        if (ParamUtil.isNotNull(abstractErrorCallBack)){
                asynGroupConfig.setAbstractErrorCallBack(abstractErrorCallBack);
        }

        return asynGroupConfig;
    }



}