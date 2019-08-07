
package com.asyncmd.service.impl;

import com.asyncmd.config.AsynCmdConfig;
import com.asyncmd.config.AsynGroupConfig;
import com.asyncmd.dao.AsynCmdDAO;
import com.asyncmd.dao.AsynCmdHistoryDAO;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.model.AsynCmdDO;
import com.asyncmd.model.AsynCmdHistoryDO;
import com.asyncmd.model.AsynQueryParam;
import com.asyncmd.model.AsynUpdateParam;
import com.asyncmd.model.Frequency;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.utils.AsynContainerUtil;
import com.asyncmd.utils.FrequencyUtil;
import com.asyncmd.utils.LocalHostUtil;
import com.asyncmd.utils.TransactionTemplateUtil;
import com.asyncmd.utils.convert.AsynCmdConvert;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author wangwendi
 * @version $Id: AsynExecuterService.java, v 0.1 2018年09月30日 wangwendi Exp $
 */
@Service
public class AsynExecuterServiceImpl  implements AsynExecuterService {


    @Autowired
    private AsynCmdDAO asynCmdDAO;

    @Autowired
    private AsynCmdHistoryDAO asynCmdHistoryDAO;
    @Autowired
    private AsynGroupConfig asynGroupConfig;
    /**
     * 插入异步命令
     * @param asynCmd
     * @return
     */
    @Override
    public long saveCmd(AsynCmd asynCmd){
        AsynCmdDO asynCmdDO = AsynCmdConvert.toDo(asynCmd);
        return asynCmdDAO.saveCmd(asynCmdDO);
    }




    /**
     * 备份异步命令
     * @param asynCmds
     */
    @Override
    public void backupCmd(final Integer tableIndex, final List<AsynCmd> asynCmds){
        final List<String> bizIds = Lists.newArrayList();
        final List<AsynCmdHistoryDO> asynCmdHistoryDOS = Lists.newArrayList();
        for (AsynCmd asynCmd : asynCmds){
            bizIds.add(asynCmd.getBizId());
            asynCmdHistoryDOS.add(AsynCmdConvert.toHistoryCmd(asynCmd));
        }
        TransactionTemplateUtil.newInstance().getTemplate().execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                long successNum = asynCmdDAO.batchDelCmd(tableIndex, bizIds);
                if (successNum != asynCmds.size()){
                    throw new AsynException(AsynExCode.SYS_ERROR,"删除异步命令失败");
                }
                long l = asynCmdHistoryDAO.batchSaveCmd(tableIndex, asynCmdHistoryDOS);
                if (l != asynCmds.size()){
                    throw new AsynException(AsynExCode.SYS_ERROR,"备份异步命令失败");
                }

            }
        });
    }

    @Override
    public boolean updateStatus(AsynUpdateParam param) {
        buildUpdateParam(param);
        long sum = asynCmdDAO.updateStatus(param);
        if (sum > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean batchUpdateStatus(AsynUpdateParam param, Integer tableIndex) {
        buildUpdateParam(param);

        Long sum = asynCmdDAO.batchUpdateStatus(tableIndex,param);
        if (sum > 0){
            return true;
        }
        return false;

    }

    @Override
    public List<AsynCmd> queryAsynCmd(Integer limit,int tableIndex,AsynStatus status,Date whereNextTime) {

        Boolean desc = asynGroupConfig.getAsynConfig().getDesc();
        AsynQueryParam param = new AsynQueryParam();
        param.setDesc(desc);
        param.setStatus(status.getStatus());
        param.setExecuterTime(whereNextTime);
        param.setEnv(asynGroupConfig.getAsynConfig().getEnv());
        List<AsynCmdDO> asynCmdDOS = asynCmdDAO.queryAsynCmd(tableIndex,param);
        return AsynCmdConvert.toCmdList(asynCmdDOS);
    }


    @Override
    public Date getNextTime(AsynCmd asynCmd) {
        int retry = asynCmd.getExecuteNum();
        //获取异步命令对象配置 如果没有单独给异步命令对象设置调度频率 则获取全局配置的调度频率
        AsynCmdConfig asynCmdConfig = AsynContainerUtil.getAsynCmdConfig(asynCmd.getClass());
        if (!CollectionUtils.isEmpty(asynCmdConfig.getExecuterFrequencyList())){
            List<Frequency> executerFrequencyList = asynCmdConfig.getExecuterFrequencyList();
            return nextTime(retry,executerFrequencyList,asynCmd);
        }
        List<Frequency> groupExecuterFrequencys = asynGroupConfig.getAsynConfig().getExecuterFrequency();
        return nextTime(retry,groupExecuterFrequencys,asynCmd);
    }

    private Date nextTime(int retry,List<Frequency> executerFrequencyList,AsynCmd asynCmd){
        Frequency frequency;
        if (executerFrequencyList.size() < retry){
            frequency = executerFrequencyList.get(executerFrequencyList.size() - 1);
        }else {
            frequency = executerFrequencyList.get(retry - 1);
        }
        return frequency.getNextTime(asynCmd.getNextTime());
    }

    @Override
    public AsynCmd getAsynCmdByBizId(String bizId,Class<? extends AsynCmd> asynCmdClass) {
        return AsynCmdConvert.toCmd(asynCmdDAO.getAsynCmdByBizId(bizId),asynCmdClass);
    }

    private void buildUpdateParam(AsynUpdateParam param){
        param.setUpdateIp(LocalHostUtil.getIp());
        param.setUpdateHostName(LocalHostUtil.getHostName());
    }


    @Override
    public Boolean relyAsynCmdSuccess(String bizId) {
        AsynCmdDO asynCmdDO = asynCmdDAO.getAsynCmdByBizId(bizId);
        if (Objects.isNull(asynCmdDO)){
            return false;
        }
        //如果依赖的异步命令未执行成功则返回失败
        if (!AsynStatus.SUCCESS.getStatus().equals(asynCmdDO.getStatus())){
            return false;
        }
        return true;
    }
}