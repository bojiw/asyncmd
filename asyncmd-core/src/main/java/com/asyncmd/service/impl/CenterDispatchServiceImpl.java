
package com.asyncmd.service.impl;

import com.asyncmd.enums.AsynStatus;
import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;

import java.util.List;

/**
 * 调度中心调度
 * @author wangwendi
 * @version $Id: CenterDispatchServiceImpl.java, v 0.1 2019年07月19日 wangwendi Exp $
 */
public class CenterDispatchServiceImpl extends AbstractDispatchService {

    @Override
    public AsynCmd buildAsynCmd(AsynCmd asynCmd, AbstractAsynExecuter<? extends AsynCmd> asynExecuter) {
        super.buildAsynCmd(asynCmd, asynExecuter);
        asynCmd.setStatus(AsynStatus.INIT.getStatus());
        asynCmd.setExecuteNum(0);
        return asynCmd;
    }

    public void dispatch(AsynCmd asynCmd, List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList) {

    }
}