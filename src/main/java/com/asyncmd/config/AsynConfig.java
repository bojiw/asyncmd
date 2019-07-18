/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.config;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.model.Frequency;
import com.asyncmd.utils.FrequencyUtil;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author wangwendi
 * @version $Id: AsynConfig.java, v 0.1 2019年07月12日 下午5:16 wangwendi Exp $
 */
public class AsynConfig {

    /**
     * 分表数量
     */
    private int tableNum = 0;

    /**
     * 一次从表中捞取命令数量
     */
    private int limit = 30;

    /**
     * 重试次数
     */
    private int retryNum = 12;

    /**
     * 如果调度模式为异步或者同步调度 则第一次无论设置多少都是立即执行
     * 重试执行频率 5s,10s,1m,1h
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次1分钟以后执行 第四次1小时以后执行 之后都是间隔1小时执行
     * 执行频率 5s,10s,20s
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次20秒以后执行
     */
    private List<Frequency> executerFrequencyList;

    private String executerFrequencys;

    private AsynJobConfig asynJobConfig = new AsynJobConfig();


    public void initConfig(){
        if (StringUtils.isEmpty(executerFrequencyList)){
            throw new AsynException(AsynExCode.EXECUTER_FREQUENCY_ILLEGAL);
        }
        executerFrequencyList = FrequencyUtil.createFrequencys(executerFrequencys);

        asynJobConfig.init(tableNum);
    }

    public int getTableNum() {
        return tableNum;
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

    /**
     * 异步命令表是否分表
     * @return
     */
    public boolean isSubTable(){
        return tableNum > 0;
    }

    public int getRetryNum() {
        return retryNum;
    }

    public void setRetryNum(int retryNum) {
        this.retryNum = retryNum;
    }
}