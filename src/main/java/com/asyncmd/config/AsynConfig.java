/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.config;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.model.Frequency;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
    private int limit = 20;

    /**
     * 如果调度模式为异步或者同步调度 则第一次无论设置多少都是立即执行
     * 执行频率 5s,10s,1m,1h
     * 代表第一次5秒以后执行 第二次10秒以后执行 第三次1分钟以后执行 第四次1小时以后执行 之后都是间隔1小时执行
     * 执行频率 5s,10s,20s
     * 代表第一次5秒以后执行 第二次10秒以后执行 第三次20秒以后执行
     */
    private List<Frequency> executerFrequencyList = new ArrayList<Frequency>();

    private String executerFrequencys;

    private AsynJobConfig asynJobConfig = new AsynJobConfig();


    public void initConfig(){
        if (StringUtils.isEmpty(executerFrequencyList)){
            throw new AsynException(AsynExCode.EXECUTER_FREQUENCY_ILLEGAL);
        }
        String[] frequencys = executerFrequencys.split(",");
        for (String frequency : frequencys){
            executerFrequencyList.add(new Frequency(frequency));
        }
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
}