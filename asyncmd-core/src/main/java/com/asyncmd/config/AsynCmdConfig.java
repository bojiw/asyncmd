
package com.asyncmd.config;

import com.asyncmd.enums.DispatchMode;
import com.asyncmd.model.Frequency;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwendi
 * @version $Id: AsynCmdConfig.java, v 0.1 2019年07月29日 下午8:49 wangwendi Exp $
 */
public class AsynCmdConfig {

    /**
     * 调度模式 默认为异步调度
     */
    private DispatchMode dispatchMode = DispatchMode.ASYN;
    /**
     * 如果有特别的异步命令不想要用全局重试频率 可以设置
     * 重试执行频率 5s,10s,1m,1h
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次1分钟以后执行 第四次1小时以后执行 之后都是间隔1小时执行
     * 执行频率 5s,10s,20s
     * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次20秒以后执行
     */

    private List<Frequency> executerFrequencyList = new ArrayList<Frequency>();

    public DispatchMode getDispatchMode() {
        return dispatchMode;
    }

    public void setDispatchMode(DispatchMode dispatchMode) {
        this.dispatchMode = dispatchMode;
    }

    public List<Frequency> getExecuterFrequencyList() {
        return executerFrequencyList;
    }

    public void setExecuterFrequencyList(List<Frequency> executerFrequencyList) {
        this.executerFrequencyList = executerFrequencyList;
    }
}