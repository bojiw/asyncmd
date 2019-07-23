
package com.asyncmd.utils;

import com.asyncmd.model.AsynCmd;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 * @author wangwendi
 * @version $Id: ParadigmUtil.java, v 0.1 2019年06月28日 wangwendi Exp $
 */
public class ParadigmUtil {

    /**
     * 获取范型类
     * @param object
     * @return
     */
    public static Class getParadigmClass(Object object){
        if (object == null){
            return null;
        }
        Type type = object.getClass().getGenericSuperclass();
        if( type instanceof ParameterizedType){
            ParameterizedType pType = (ParameterizedType)type;
            Type claz = pType.getActualTypeArguments()[0];
            if (claz instanceof Class){
                return (Class<? extends AsynCmd>) claz;
            }
        }
        return null;

    }
}