
package com.asyncmd.model;

import com.asyncmd.enums.DispatchMode;
import com.asyncmd.manager.AsynExecuterFacade;
import com.asyncmd.utils.FrequencyUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 异步命令执行器
 *
 * @author wangwendi
 * @version $Id: AbstractAsynExecuter.java, v 0.1 2018年09月20日 wangwendi Exp $
 */
public abstract class AbstractAsynExecuter<T extends AsynCmd> implements InitializingBean {
    /**
     * 调度模式 默认为异步调度
     */
    protected DispatchMode dispatchMode = DispatchMode.ASYN;



    @Autowired
    private AsynExecuterFacade asynExecuterFacade;


    /**
     * 如果有特别的异步命令不想要用全局重试频率 可以设置
     * 重试执行频率 5s,10s,1m,1h
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次1分钟以后执行 第四次1小时以后执行 之后都是间隔1小时执行
     * 执行频率 5s,10s,20s
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次20秒以后执行
     */
    protected String executerFrequencys;

    private List<Frequency> executerFrequencyList = new ArrayList<Frequency>();

    /**
     * 异步命令器执行顺序 越大越早执行 获取异步命令的相关配置默认取第一个
     */
    protected int sort = 100;




    /**
     * 由子类执行具体逻辑
     * @param cmd
     */
    protected void executer(T cmd){};


    /**
     * 初始化的时候注册到执行器容器中
     * @throws Exception
     */
    final public void afterPropertiesSet() throws Exception {
        if (!StringUtils.isEmpty(executerFrequencys)){
            executerFrequencyList = FrequencyUtil.createFrequencys(executerFrequencys);
        }
        //注册到容器中
        asynExecuterFacade.registerAsynExecuter(this);
    }


    public void setExecuterFrequencys(String executerFrequencys) {
        this.executerFrequencys = executerFrequencys;
    }

    public void setDispatchMode(DispatchMode dispatchMode) {
        this.dispatchMode = dispatchMode;
    }

    public DispatchMode getDispatchMode() {
        return dispatchMode;
    }

    final public List<Frequency> getExecuterFrequencyList() {
        return executerFrequencyList;
    }
    protected int getSort(){
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

}

