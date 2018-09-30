/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.manager.impl;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.manager.AsynExecuterFacade;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynExecuter;
import com.asyncmd.service.impl.AsynExecuterService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 *
 * @author wangwendi
 * @version $Id: AsynExecuterFacadeImpl.java, v 0.1 2018年09月30日 wangwendi Exp $
 */
public class AsynExecuterFacadeImpl implements AsynExecuterFacade {

    private static Log log = LogFactory.getLog(AsynExecuterFacadeImpl.class);

    private AsynExecuterService asynExecuterService;


    public AsynExecuterFacadeImpl(){
        asynExecuterService = new AsynExecuterService();
    }


    public void registerAsynExecuter(AsynExecuter<? extends AsynCmd> asynExecuter) {

        Class<? extends AsynCmd> classCmd = getAsynCmd(asynExecuter);
        //效验
        vaildAsynCmd(classCmd,asynExecuter);
        //注册
        asynExecuterService.getAsynExecuterMap().put(classCmd,asynExecuter);

    }

    /**
     * 效验异步命令对象是否能正常初始化
     * @param classCmd
     */
    private void vaildAsynCmd(Class<? extends AsynCmd> classCmd,AsynExecuter<? extends AsynCmd> asynExecuter){

        try {
            classCmd.newInstance();

        }catch (Exception e){
            log.error("执行器上的asynCmd初始化异常:" + asynExecuter.getClass().getName());
            throw new AsynException(AsynExCode.ILLEGAL);
        }

    }

    /**
     * 获取执行器上的范型类
     * @param asynExecuter
     * @return
     */
    private Class<? extends AsynCmd> getAsynCmd(AsynExecuter<? extends AsynCmd> asynExecuter){
        Type type = asynExecuter.getClass().getGenericSuperclass();
        if( type instanceof ParameterizedType){
            ParameterizedType pType = (ParameterizedType)type;
            Type claz = pType.getActualTypeArguments()[0];
            if( claz instanceof Class ){
                return (Class<? extends AsynCmd>) claz;
            }
        }
        log.error("获取执行器上的asynCmd异常:" + asynExecuter.getClass().getName());
        throw new AsynException(AsynExCode.ILLEGAL);

    }
}