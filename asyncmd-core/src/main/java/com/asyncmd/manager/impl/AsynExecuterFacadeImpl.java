
package com.asyncmd.manager.impl;

import com.asyncmd.config.AsynCmdConfig;
import com.asyncmd.enums.DispatchMode;
import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.manager.AsynExecuterFacade;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.service.DispatchService;
import com.asyncmd.utils.AsynContainerUtil;
import com.asyncmd.utils.DispatchFactory;
import com.asyncmd.utils.ParadigmUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 *
 *
 * @author wangwendi
 * @version $Id: AsynExecuterFacadeImpl.java, v 0.1 2018年09月30日 wangwendi Exp $
 */
public class AsynExecuterFacadeImpl implements AsynExecuterFacade {

    /**
     * 唯一索引冲突code
     */
    private static final int DUPLICATE_CODE = 1062;
    private static Log log = LogFactory.getLog(AsynExecuterFacadeImpl.class);

    @Autowired
    private AsynExecuterService asynExecuterService;


    @Autowired
    private DispatchFactory dispatchFactory;



    @Override
    public void registerAsynExecuter(AbstractAsynExecuter<? extends AsynCmd> asynExecuter) {

        Class<? extends AsynCmd> classCmd = getAsynCmdObject(asynExecuter);
        //效验
        vaildAsynCmd(classCmd, asynExecuter);
        //保存异步命令对象配置
        AsynContainerUtil.put(classCmd);

        //注册
        AsynContainerUtil.put(classCmd,asynExecuter);
        AsynContainerUtil.getAsynCmdNameMapping().put(classCmd.getSimpleName(),classCmd);

    }

    @Override
    public void saveExecuterAsynCmd(AsynCmd asynCmd) {
        validation(asynCmd);
        List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuters = AsynContainerUtil.getAsynExecuterMap().get(asynCmd.getClass());
        if (CollectionUtils.isEmpty(asynExecuters)){
            throw new AsynException(AsynExCode.ILLEGAL,"根据asynCmd获取不到对应的执行器,检查执行器是否有配置由spring管理:" + asynCmd.getClass().getName());
        }
        AsynCmdConfig asynCmdConfig = AsynContainerUtil.getAsynCmdConfig(asynCmd.getClass());
        if (asynCmdConfig.getDispatchMode() == null){
            throw new AsynException(AsynExCode.ILLEGAL,asynCmd.getClass().getSimpleName() + "的dispatchMode不能为空");
        }
        DispatchService dispatchService = dispatchFactory.getDispatchService(asynCmdConfig.getDispatchMode().getStatus());
        dispatchService.buildAsynCmd(asynCmd);
        try {
            asynExecuterService.saveCmd(asynCmd);
        }catch (org.springframework.dao.DuplicateKeyException e){
            //代表是重复插入 不抛异常
            log.warn("保存异步命令幂等异常bizId=" + asynCmd.getBizId());
            return;
        }
        //进行调度执行
        dispatchService.dispatch(asynCmd,asynExecuters);
    }

    private void validation(AsynCmd asynCmd){
        if (asynCmd == null){
            throw new AsynException(AsynExCode.ILLEGAL,"入参数不能为空");
        }
        if (StringUtils.isEmpty(asynCmd.getBizId())){
            throw new AsynException(AsynExCode.ILLEGAL,"bizId不能为空");
        }
        if (asynCmd.getContent() == null){
            throw new AsynException(AsynExCode.ILLEGAL,"content不能为空");
        }

    }


    /**
     * 效验异步命令对象是否能正常初始化
     * @param classCmd
     */
    private void vaildAsynCmd(Class<? extends AsynCmd> classCmd,AbstractAsynExecuter<? extends AsynCmd> asynExecuter){

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