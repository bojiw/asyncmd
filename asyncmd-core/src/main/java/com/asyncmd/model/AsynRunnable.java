
package com.asyncmd.model;

import com.alibaba.fastjson.JSON;
import com.asyncmd.callback.ErrorCallBack;
import com.asyncmd.config.AsynGroupConfig;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import com.asyncmd.service.AsynExecuterService;
import com.asyncmd.utils.CountException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
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
            valideRelyAsynCmdSuccess(asynCmd.getRelyBizId());
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
            AsynStatus asynStatus = updateStatus(e);
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

    /**
     * 效验依赖异步命令对象是否执行成功
     * @param relyBizId
     */
    private void valideRelyAsynCmdSuccess(String relyBizId){
        if (StringUtils.isEmpty(relyBizId)){
            return;
        }
        if (!asynExecuterService.relyAsynCmdSuccess(asynCmd.getRelyBizId())){
            throw new AsynException(AsynExCode.RELY_NO_EXECUTER);
        }
    }


    /**
     * 获取堆栈信息
     * @param e
     * @return
     */
    private String getException(Exception e){

        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String s = sw.toString();
            sw.close();
            pw.close();
            if (s.length() > AsynCmd.EXCEPTION){
                return s.substring(0, AsynCmd.EXCEPTION);
            }
            return s;
        } catch (Exception ex) {
            return "获得Exception信息的工具类异常";
        }
    }

    private AsynStatus updateStatus(Exception e){
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
        asynUpdateParam.setException(getException(e));
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