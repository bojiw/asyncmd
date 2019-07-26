
package com.asyncmd.config;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.model.Frequency;
import com.asyncmd.utils.FrequencyUtil;
import com.asyncmd.utils.LocalHostUtil;
import com.asyncmd.utils.SubTableUtil;
import com.asyncmd.utils.ThreadPoolTaskExecutorUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author wangwendi
 * @version $Id: AsynConfig.java, v 0.1 2019年07月12日 wangwendi Exp $
 */
public class AsynConfig {
    private static Log log = LogFactory.getLog(AsynConfig.class);

    /**
     * 分表数量 如果设置需要是2的倍数如 8 16 32 64
     */
    private int tableNum = 0;

    /**
     * 一次从表中捞取命令数量
     */
    private int limit = 20;

    /**
     * 重试次数
     */
    private int retryNum = 12;

    /**
     * 异步命令是否要先进后出 默认先进先出 注意这个只针对一张表的情况
     */
    private Boolean desc = false;

    /**
     * 重试执行频率 5s,10s,1m,1h
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次1分钟以后执行 第四次1小时以后执行 之后都是间隔1小时执行
     * 执行频率 5s,10s,20s
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次20秒以后执行
     */
    private List<Frequency> executerFrequencyList;

    private String executerFrequencys;

    private AsynJobConfig asynJobConfig = new AsynJobConfig();



    public void initConfig(){
        beforeInit();
        if (StringUtils.isEmpty(executerFrequencys)){
            log.error(AsynExCode.EXECUTER_FREQUENCY_ILLEGAL.getMessage());
            throw new AsynException(AsynExCode.EXECUTER_FREQUENCY_ILLEGAL);
        }
        executerFrequencyList = FrequencyUtil.createFrequencys(executerFrequencys);

        asynJobConfig.init(tableNum);
    }

    private void beforeInit(){
        //需要先初始化分表工具类
        SubTableUtil.init(tableNum);
        LocalHostUtil.init();
        ThreadPoolTaskExecutorUtil.newInstance().init();
    }

    public List<Frequency> getExecuterFrequency() {
        return executerFrequencyList;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    public void setExecuterFrequencys(String executerFrequencys) {
        this.executerFrequencys = executerFrequencys;
    }

    public AsynJobConfig getAsynJobConfig() {
        return asynJobConfig;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public int getRetryNum() {
        return retryNum;
    }

    public void setRetryNum(int retryNum) {
        this.retryNum = retryNum;
    }

    public void setDesc(Boolean desc) {
        this.desc = desc;
    }

    public Boolean getDesc() {
        return desc;
    }


}