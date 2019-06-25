/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.manager.impl;

import com.asyncmd.enums.AsynStatus;
import com.asyncmd.enums.DispatchMode;
import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.manager.AsynExecuterFacade;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynExecuter;
import com.asyncmd.service.impl.AsynExecuterService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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

    public void saveExecuterAsynCmd(AsynCmd asynCmd) {
        AsynExecuter<? extends AsynCmd> asynExecuter = asynExecuterService.getAsynExecuterMap()
                .get(asynCmd.getClass());
        if (asynExecuter == null){
            log.error("根据asynCmd获取不到对应的执行器,检查执行器是否有注册:" + asynCmd.getClass().getName());
            throw new AsynException(AsynExCode.ILLEGAL);
        }
        DispatchMode dispatchMode = asynExecuter.getDispatchMode();
        //如果是要立刻执行 则直接设置状态为执行中
        boolean executer = false;
        if (DispatchMode.DEFAULT != dispatchMode){
            asynCmd.setStatus(AsynStatus.EXECUTE.getStatus());
            executer = true;
        }
        try {
            asynExecuterService.saveCmd(asynCmd);
        }catch (DataIntegrityViolationException e){
            //TODO 需要修改为code MySQLIntegrityConstraintViolationException
            if (e.getLocalizedMessage().contains("Duplicate")){
                //如果是因为唯一性索引导致插入命令失败 代表是重复插入 则直接返回 不抛异常
                return;
            }
            throw e;

        }
        //如果不需要立刻执行 直接返回
        if (!executer){
            return;
        }
        executer(asynExecuter,asynCmd);


    }

    /**
     * 执行异步命令
     * @param asynExecuter
     * @param asynCmd
     */
    private void executer(AsynExecuter<? extends AsynCmd> asynExecuter,AsynCmd asynCmd){

        switch (asynExecuter.getDispatchMode()){
            case ASYN :
                asynExecuter.asynExecuter(asynCmd);
                break;
            case ASY:
                asyExecuter(asynExecuter,asynCmd);
                break;
            default:
        }
    }

    /**
     * 同步执行
     * @param asynExecuter
     * @param asynCmd
     */
    private void asyExecuter(AsynExecuter<? extends AsynCmd> asynExecuter,AsynCmd asynCmd){
        if (TransactionSynchronizationManager.isActualTransactionActive()){
            //如果在事务内 则在事务的回调中执行 防止执行器中也有事务导致嵌套事务
            TransactionSynchronizationManager
                    .registerSynchronization(new AsyTransactionSynchronization(asynCmd,asynExecuter));

        }else {
            asynExecuter.asyExecuter(asynCmd,false);
        }

    }

    class AsyTransactionSynchronization implements TransactionSynchronization{

        private AsynCmd asynCmd;
        private AsynExecuter asynExecuter;

        AsyTransactionSynchronization(AsynCmd asynCmd,AsynExecuter asynExecuter){
            this.asynCmd = asynCmd;
            this.asynExecuter = asynExecuter;
        }

        public void afterCompletion(int i) {
            //如果事务已经提交 则同步执行命令
            if (TransactionSynchronization.STATUS_COMMITTED == i){
                asynExecuter.asyExecuter(asynCmd,true);
            }
        }

        public void suspend() {

        }

        public void resume() {

        }

        public void flush() {

        }

        public void beforeCommit(boolean b) {

        }

        public void beforeCompletion() {

        }

        public void afterCommit() {

        }
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