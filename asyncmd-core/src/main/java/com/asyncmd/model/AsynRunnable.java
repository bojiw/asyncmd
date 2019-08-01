
package com.asyncmd.model;

import com.alibaba.fastjson.JSON;
import com.asyncmd.callback.ErrorCallBack;
import com.asyncmd.config.AsynGroupConfig;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.utils.CountException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @author wangwendi
 * @version $Id: AsynRunnable.java, v 0.1 2019年07月20日 wangwendi Exp $
 */
public class AsynRunnable implements Runnable {

    private static Log log = LogFactory.getLog(AsynRunnable.class);

    private AsynCmd asynCmd;
    private CountException countException;
    private List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList;
    private AsynGroupConfig asynGroupConfig;
    private AsynExecuterService asynExecuterService;


    public AsynRunnable asynCmd(AsynCmd asynCmd){
        this.asynCmd = asynCmd;
        return this;
    }
    public AsynRunnable countException(CountException countException){
        this.countException = countException;
        return this;
    }
    public AsynRunnable asynExecuterList(List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList){
        this.asynExecuterList = asynExecuterList;
        return this;
    }
    public AsynRunnable asynGroupConfig(AsynGroupConfig asynGroupConfig){
        this.asynGroupConfig = asynGroupConfig;
        return this;
    }
    public AsynRunnable asynExecuterService(AsynExecuterService asynExecuterService){
        this.asynExecuterService = asynExecuterService;
        return this;
    }

    @Override
    public void run() {

        try {
            for (AbstractAsynExecuter asynExecuter : asynExecuterList){
                //如果已经成功执行可以不执行
                if (isSuccess(asynExecuter,asynCmd)){
                    continue;
                }
                asynExecuter.executer(asynCmd);
                //执行成功则把执行成功的类名更新上去
                asynCmd.addSuccessExecuter(asynExecuter.getClass().getSimpleName());
            }
            AsynUpdateParam param = new AsynUpdateParam();
            param.setStatus(AsynStatus.SUCCESS.getStatus());
            param.setWhereAsynStatus(AsynStatus.EXECUTE.getStatus());
            param.setBizId(asynCmd.getBizId());
            param.setSuccessExecutes(JSON.toJSONString(asynCmd.getSuccessExecuters()));
            asynExecuterService.updateStatus(param);
        }catch (Exception e){
            //失败更新状态为初始化或错误 初始化状态则由调度中心调度
            AsynStatus asynStatus = updateStatus();
            if (countNotNull()){
                countException.setException(new AsynException(AsynExCode.SYS_ERROR,e));
            }
            callBack(asynStatus,e);
        }finally {
            if (countNotNull()){
                countException.countDown();
            }
        }
    }

    private AsynStatus updateStatus(){
        AsynUpdateParam asynUpdateParam = new AsynUpdateParam();
        AsynStatus asynStatus;
        if (asynCmd.getExecuteNum() >= asynGroupConfig.getAsynConfig().getRetryNum()){
            asynStatus = AsynStatus.ERROR;
            asynUpdateParam.setStatus(AsynStatus.ERROR.getStatus());

        }else {
            asynStatus = AsynStatus.INIT;
            asynUpdateParam.setStatus(AsynStatus.INIT.getStatus());
            //默认取第一个执行器配置
            asynUpdateParam.setNextTime(asynExecuterService.getNextTime(asynCmd));

        }
        asynUpdateParam.setBizId(asynCmd.getBizId());
        asynUpdateParam.setWhereAsynStatus(AsynStatus.EXECUTE.getStatus());
        asynUpdateParam.setSuccessExecutes(JSON.toJSONString(asynCmd.getSuccessExecuters()));

        asynExecuterService.updateStatus(asynUpdateParam);
        return asynStatus;
    }

    /**
     * 出现异常时回调
     * @param status
     */
    private void callBack(AsynStatus status,Exception e){
        try {
            ErrorCallBack errorCallBack = asynGroupConfig.getErrorCallBack();
            if (errorCallBack == null){
                return;
            }
            errorCallBack.everyErrorCallBack(asynCmd,e);
            if (status == AsynStatus.ERROR){
                errorCallBack.errorCallBack(asynCmd,e);
            }

        }catch (Exception ex){
            log.error("调用异常回调接口异常,bizId=" + asynCmd.getBizId(),ex);
        }
    }

    /**
     * 是否已经成功执行过
     * @param asynExecuter
     * @param asynCmd
     * @return
     */
    private boolean isSuccess(AbstractAsynExecuter asynExecuter,AsynCmd asynCmd){
        return asynCmd.getSuccessExecuters().contains(asynExecuter.getClass().getSimpleName());
    }
    private boolean countNotNull(){
        return countException != null;
    }

}