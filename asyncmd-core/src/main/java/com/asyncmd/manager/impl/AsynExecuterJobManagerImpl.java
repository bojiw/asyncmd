
package com.asyncmd.manager.impl;

import com.asyncmd.config.AsynGroupConfig;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.manager.AsynExecuterJobManager;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynUpdateParam;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.service.impl.AsynDispatchServiceImpl;
import com.asyncmd.utils.AsynExecuterUtil;
import com.asyncmd.utils.ThreadPoolTaskExecutorUtil;
import com.asyncmd.utils.TransactionTemplateUtil;
import com.asyncmd.utils.convert.AsynCmdConvert;
import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author wangwendi
 * @version $Id: AsynExecuterJobManagerImpl.java, v 0.1 2019年07月16日 wangwendi Exp $
 */
@Service
public class AsynExecuterJobManagerImpl implements AsynExecuterJobManager {
    private static Log log = LogFactory.getLog(AsynExecuterJobManagerImpl.class);


    @Autowired
    private AsynExecuterService asynExecuterService;

    @Autowired
    private AsynGroupConfig asynGroupConfig;

    @Autowired
    private AsynDispatchServiceImpl asynDispatchService;

    public void executer(int tableIndex) {

        asynExecuter(tableIndex);
    }


    /**
     * 异步命令执行
     */
    private void asynExecuter(final int tableIndex){
        Integer limit;
        if (asynGroupConfig.getAsynConfig().getLimit() > getPoolSurplusNum()){
            log.warn("asyn线程池不足,请注意,剩余线程数"+ getPoolSurplusNum());
            limit = getPoolSurplusNum();
        }else {
            limit = asynGroupConfig.getAsynConfig().getLimit();
        }
        final List<AsynCmd> asynCmdList = asynExecuterService.queryAsynCmd(limit,tableIndex,AsynStatus.INIT,getNextMillis());
        final List<String> bizIds = AsynCmdConvert.toBizIds(asynCmdList);
        if (CollectionUtils.isEmpty(bizIds)){
            return;
        }
        //更新状态为执行中
        final Boolean updateSuccess = TransactionTemplateUtil.newInstance().getTemplate().execute(new TransactionCallback<Boolean>() {
            public Boolean doInTransaction(TransactionStatus status) {
                AsynUpdateParam param = new AsynUpdateParam();
                param.setBizIds(bizIds);
                param.setExecuter(true);
                param.setStatus(AsynStatus.EXECUTE.getStatus());
                param.setWhereAsynStatus(AsynStatus.INIT.getStatus());
                return asynExecuterService.batchUpdateStatus(param, tableIndex);

            }
        });
        //如果更新失败 则直接返回
        if (updateSuccess == null || !updateSuccess){
            return;
        }
        pushPool(asynCmdList);
        //重置移到底层
        // //push线程池失败的异步命令业务id
        // final List<String> failBizIds = AsynCmdConvert.toBizIds(failList);
        // if (CollectionUtils.isEmpty(failBizIds)){
        //     return;
        // }
        // //推送线程池失败的命令状态重置
        // TransactionTemplateUtil.newInstance().getTemplate().execute(new TransactionCallbackWithoutResult() {
        //     @Override
        //     protected void doInTransactionWithoutResult(TransactionStatus status) {
        //         AsynUpdateParam failParam = new AsynUpdateParam();
        //         failParam.setBizIds(failBizIds);
        //         failParam.setReset(true);
        //         failParam.setStatus(AsynStatus.INIT.getStatus());
        //         failParam.setWhereAsynStatus(AsynStatus.EXECUTE.getStatus());
        //         asynExecuterService.batchUpdateStatus(failParam,tableIndex);
        //     }
        // });
    }
    private Date getNextMillis(){
        return new Date(System.currentTimeMillis() + 1000);
    }



    private int getPoolSurplusNum(){
        ThreadPoolTaskExecutor poolTaskExecutor = ThreadPoolTaskExecutorUtil.newInstance().getPoolTaskExecutor();
        return poolTaskExecutor.getMaxPoolSize() - poolTaskExecutor.getActiveCount();
    }


    private List<AsynCmd> pushPool(List<AsynCmd> asynCmdList){
        List<AsynCmd> failList = Lists.newArrayList();
        for (AsynCmd asynCmd : asynCmdList){
            //内存中的执行次数+1
            asynCmd.setExecuteNum(asynCmd.getExecuteNum() + 1);
            List<AbstractAsynExecuter<? extends AsynCmd>> abstractAsynExecuters = AsynExecuterUtil.getAsynExecuterMap().get(asynCmd.getClass());
            if (!asynDispatchService.asynExecuter(asynCmd,abstractAsynExecuters)){
                failList.add(asynCmd);
            }
        }
        return failList;
    }

}