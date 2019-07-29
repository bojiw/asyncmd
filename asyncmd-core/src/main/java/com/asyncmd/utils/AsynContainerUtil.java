
package com.asyncmd.utils;

import com.asyncmd.config.AsynCmdConfig;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynExecuterComparator;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 异步命令容器
 * @author wangwendi
 * @version $Id: AsynContainerUtil.java, v 0.1 2018年09月30日  wangwendi Exp $
 */
public class AsynContainerUtil {

    private static Map<Class<? extends AsynCmd>,List<AbstractAsynExecuter<? extends AsynCmd>>> asynExecuterMap = new ConcurrentHashMap<Class<? extends
            AsynCmd>, List<AbstractAsynExecuter<? extends AsynCmd>>>();

    private static Map<String,Class<? extends AsynCmd>> asynCmdNameMapping = new ConcurrentHashMap<String, Class<? extends AsynCmd>>();

    private static Map<Class<? extends AsynCmd>,AsynCmdConfig> asynCmdConfigContainer = new ConcurrentHashMap<>();

    public static Map<Class<? extends AsynCmd>, List<AbstractAsynExecuter<? extends AsynCmd>>> getAsynExecuterMap() {
        return asynExecuterMap;
    }

    public static Map<String, Class<? extends AsynCmd>> getAsynCmdNameMapping() {
        return asynCmdNameMapping;
    }

    public static void put(Class<? extends AsynCmd> classCmd,AbstractAsynExecuter<? extends AsynCmd> asynExecuter){
        List<AbstractAsynExecuter<? extends AsynCmd>> abstractAsynExecuters = asynExecuterMap.get(classCmd);
        if (CollectionUtils.isEmpty(abstractAsynExecuters)){
            List<AbstractAsynExecuter<? extends AsynCmd>> newAbstractAsynExecuters = Lists.newArrayList();
            newAbstractAsynExecuters.add(asynExecuter);
            asynExecuterMap.put(classCmd,newAbstractAsynExecuters);
            return;
        }
        abstractAsynExecuters.add(asynExecuter);
    }

    public static void put(Class<? extends AsynCmd> classCmd,AsynCmd asynCmd){
        AsynCmdConfig asynCmdConfig = new AsynCmdConfig();
        if (asynCmd.getDispatchMode() != null){
            asynCmdConfig.setDispatchMode(asynCmd.getDispatchMode());
        }
        if (!StringUtils.isEmpty(asynCmd.getExecuterFrequencys())){
            asynCmdConfig.setExecuterFrequencyList(FrequencyUtil.createFrequencys(asynCmd.getExecuterFrequencys()));
        }
        asynCmdConfigContainer.put(classCmd,asynCmdConfig);
    }

    public static AsynCmdConfig getAsynCmdConfig(Class<? extends AsynCmd> classCmd){
        return asynCmdConfigContainer.get(classCmd);
    }

    /**
     * 把所以异步命令执行器进行排序
     */
    public static void init(){
        if (asynExecuterMap.isEmpty()){
            return;
        }
        Iterator<Entry<Class<? extends AsynCmd>, List<AbstractAsynExecuter<? extends AsynCmd>>>> iterator = asynExecuterMap.entrySet().iterator();
        while (iterator.hasNext()){
            List<AbstractAsynExecuter<? extends AsynCmd>> value = iterator.next().getValue();
            Collections.sort(value,new AsynExecuterComparator());
        }

    }
}