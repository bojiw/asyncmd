package com.asyncmd.starter;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wangwendi
 * @date 2019/8/12
 */
public class ParamUtil {

    public static void assertNull(Object object,String msg){
        if (object == null){
            throw new AsynBootException(msg);
        }
    }
    public static void assertStringNull(String object,String msg){
        if (StringUtils.isEmpty(object)){
            throw new AsynBootException(msg);
        }
    }

    public static Boolean isNotNull(Object o){
        return o != null;
    }

    public static Boolean isNotEmpty(String o){
        return StringUtils.isNotEmpty(o);
    }

}