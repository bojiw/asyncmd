
package com.asyncmd.utils;

import com.asyncmd.model.Frequency;
import com.google.common.collect.Lists;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author wangwendi
 * @version $Id: FrequencyUtil.java, v 0.1 2019年07月17日 wangwendi Exp $
 */
public class FrequencyUtil {

    public static List<Frequency> createFrequencys(String executerFrequencys){
        if (StringUtils.isEmpty(executerFrequencys)){
            return Lists.newArrayList();
        }
        List<Frequency> executerFrequencyList = Lists.newArrayList();
        String[] frequencys = executerFrequencys.split(",");
        for (String frequency : frequencys){
            executerFrequencyList.add(new Frequency(frequency));
        }
        return executerFrequencyList;
    }
}