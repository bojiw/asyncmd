/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.asyncmd.manager.impl;

import com.asyncmd.config.GroupConfig;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.enums.DispatchMode;
import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.manager.AsynExecuterFacade;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.utils.ParadigmUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 *
 * @author wangwendi
 * @version $Id: AsynExecuterFacadeImpl.java, v 0.1 2018年09月30日 wangwendi Exp $
 */
public class AsynExecuterFacadeImpl implements AsynExecuterFacade,InitializingBean {

    /**
     * 唯一索引冲突code
     */
    private static final int DUPLICATE_CODE = 1062;
    private static Log log = LogFactory.getLog(AsynExecuterFacadeImpl.class);

    @Autowired
    private AsynExecuterService asynExecuterService;

    @Autowired
    private GroupConfig groupConfig;


    @Override
    public void afterPropertiesSet() throws Exception {
        groupConfig.init();
    }


    @Override
    public void registerAsynExecuter(AbstractAsynExecuter<? extends AsynCmd> asynExecuter) {

        Class classCmd = getAsynCmdObject(asynExecuter);
        //效验
        vaildAsynCmd(classCmd,asynExecuter);
        //注册
        asynExecuterService.getAsynExecuterMap().put(classCmd,asynExecuter);

    }

    @Override
    public void saveExecuterAsynCmd(AsynCmd asynCmd) {
        AbstractAsynExecuter<? extends AsynCmd> asynExecuter = asynExecuterService.getAsynExecuterMap()
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
        }catch (org.springframework.dao.DuplicateKeyException e){
            //代表是重复插入 不抛异常
            log.warn("保存异步命令幂等异常bizId=" + asynCmd.getBizId());
            return;
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
    private void executer(AbstractAsynExecuter<? extends AsynCmd> asynExecuter, AsynCmd asynCmd){

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
    private void asyExecuter(AbstractAsynExecuter<? extends AsynCmd> asynExecuter, AsynCmd asynCmd){
        if (TransactionSynchronizationManager.isActualTransactionActive()){
            //如果在事务内 则在事务的回调中执行 防止执行器中也有事务导致嵌套事务
            TransactionSynchronizationManager
                    .registerSynchronization(new AsyTransactionSynchronization(asynCmd,asynExecuter));

        }else {
            asynExecuter.asyExecuter(asynCmd,false);
        }

    }

    class AsyTransactionSynchronization implements TransactionSynchronization{

        private AsynCmd              asynCmd;
        private AbstractAsynExecuter asynExecuter;

        AsyTransactionSynchronization(AsynCmd asynCmd,AbstractAsynExecuter asynExecuter){
            this.asynCmd = asynCmd;
            this.asynExecuter = asynExecuter;
        }

        @Override
        public void afterCompletion(int i) {
            //如果事务已经提交 则同步执行命令
            if (TransactionSynchronization.STATUS_COMMITTED == i){
                asynExecuter.asyExecuter(asynCmd,true);
            }
        }
        @Override
        public void suspend() {

        }
        @Override
        public void resume() {

        }
        @Override
        public void flush() {

        }
        @Override
        public void beforeCommit(boolean b) {

        }
        @Override
        public void beforeCompletion() {

        }
        @Override
        public void afterCommit() {

        }
    }


    /**
     * 效验异步命令对象是否能正常初始化
     * @param classCmd
     */
    private void vaildAsynCmd(Class classCmd,AbstractAsynExecuter<? extends AsynCmd> asynExecuter){

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
    private Class getAsynCmdObject(AbstractAsynExecuter<? extends AsynCmd> asynExecuter){
        Class paradigmClass = ParadigmUtil.getParadigmClass(asynExecuter);
        if (paradigmClass != null){
            return paradigmClass;
        }
        log.error("获取执行器上的asynCmd异常:" + asynExecuter.getClass().getName());
        throw new AsynException(AsynExCode.ILLEGAL);

    }

    /**
     * 获取异步命令对象上的范型类
     * @param asynCmd
     * @return
     */
    private Class getAsynCmdObject(AsynCmd asynCmd){
        Class asynObject = ParadigmUtil.getParadigmClass(asynCmd);
        if (asynObject != null){
            return asynObject;
        }
        log.error("获取执行器上的asynCmd异常:" + asynCmd.getClass().getName());
        throw new AsynException(AsynExCode.ILLEGAL);

    }



}