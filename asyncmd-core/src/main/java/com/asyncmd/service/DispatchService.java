
package com.asyncmd.service;

import com.asyncmd.model.AbstractAsynExecuter;
import com.asyncmd.model.AsynCmd;

import java.util.List;

/**
 *
 * 调度执行服务
 * @author wangwendi
 * @version $Id: DispatchService.java, v 0.1 2019年07月19日 wangwendi Exp $
 */
public interface DispatchService {

    void dispatch(AsynCmd asynCmd,List<AbstractAsynExecuter<? extends AsynCmd>> asynExecuterList);

    /**
     * 拼装异步命令
     * @param asynCmd
     * @return
     */
    AsynCmd buildAsynCmd(AsynCmd asynCmd);
}