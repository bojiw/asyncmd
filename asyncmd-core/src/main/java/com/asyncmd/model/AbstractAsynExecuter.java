
package com.asyncmd.model;

import com.asyncmd.manager.AsynExecuterFacade;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 异步命令执行器
 *
 * @author wangwendi
 * @version $Id: AbstractAsynExecuter.java, v 0.1 2018年09月20日 wangwendi Exp $
 */
public abstract class AbstractAsynExecuter<T extends AsynCmd> implements InitializingBean {

    @Autowired
    private AsynExecuterFacade asynExecuterFacade;

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
    @Override
    final public void afterPropertiesSet() throws Exception {
        //注册到容器中
        asynExecuterFacade.registerAsynExecuter(this);
    }

    protected int getSort(){
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

}

