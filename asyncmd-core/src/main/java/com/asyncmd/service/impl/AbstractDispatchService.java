
package com.asyncmd.service.impl;

import com.asyncmd.config.AsynGroupConfig;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynRunnable;
import com.asyncmd.model.AsynUpdateParam;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.service.DispatchService;
import com.asyncmd.utils.CountException;
import com.asyncmd.utils.LocalExceptionUtil;
import com.asyncmd.utils.LocalHostUtil;
import com.asyncmd.utils.ThreadPoolTaskExecutorUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author wangwendi
 * @version $Id: AbstractDispatchService.java, v 0.1 2019年07月19日 wangwendi Exp $
 */
public abstract class AbstractDispatchService implements DispatchService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private AsynExecuterService asynExecuterService;
    @Autowired
    private AsynGroupConfig asynGroupConfig;


    protected AsynRunnable createAsynRunnable(AsynCmd asynCmd, List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList
            ,CountException countException){
        return new AsynRunnable().asynCmd(asynCmd)
                .asynGroupConfig(asynGroupConfig)
                .asynExecuterService(asynExecuterService)
                .asynExecuterList(asynExecuterList)
                .countException(countException);
    }

    protected AsynRunnable createAsynRunnable(AsynCmd asynCmd, List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList){
        return new AsynRunnable().asynCmd(asynCmd)
                .asynGroupConfig(asynGroupConfig)
                .asynExecuterService(asynExecuterService)
                .asynExecuterList(asynExecuterList);
    }


    @Override
    public AsynCmd buildAsynCmd(AsynCmd asynCmd) {
        asynCmd.setCmdType(asynCmd.getClass().getSimpleName());
        if (asynCmd.getNextTime() == null){
            asynCmd.setNextTime(new Date());
        }
        if (asynCmd.getCreateName() == null){
            asynCmd.setCreateName(AsynCmd.default_create_name);
        }
        asynCmd.setStatus(AsynStatus.EXECUTE.getStatus());
        asynCmd.setExecuteNum(1);
        asynCmd.setCreateHostName(LocalHostUtil.getHostName());
        asynCmd.setCreateIp(LocalHostUtil.getIp());
        asynCmd.setUpdateHostName(LocalHostUtil.getHostName());
        asynCmd.setUpdateIp(LocalHostUtil.getIp());
        return asynCmd;
    }

    /**
     * 线程池中执行异步命令
     * @param asynCmd
     * @param asynExecuterList
     */
    public boolean poolAsynExecuter(AsynCmd asynCmd,List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList){
        return poolAsynExecuter(asynCmd,asynExecuterList,null);

    }
    /**
     * 线程池中执行异步命令
     * @param asynCmd
     * @param asynExecuterList
     */
    public boolean poolAsynExecuter(AsynCmd asynCmd, List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList, CountException countException){
        try {
            AsynRunnable asynRunnable = createAsynRunnable(asynCmd,asynExecuterList,countException);
            ThreadPoolTaskExecutorUtil.newInstance().getPoolTaskExecutor().execute(asynRunnable);
        }catch (Exception e){
            //放入线程池失败则状态为回滚
            AsynUpdateParam param = new AsynUpdateParam();
            param.setBizId(asynCmd.getBizId());
            param.setStatus(AsynStatus.INIT.getStatus());
            param.setWhereAsynStatus(AsynStatus.EXECUTE.getStatus());
            param.setReset(true);
            asynExecuterService.updateStatus(param);
            log.warn("asyn线程池异常",e);
            LocalExceptionUtil.set(new AsynException(AsynExCode.THREAD_POLL_ERROR));
            return false;
        }
        return true;

    }
}