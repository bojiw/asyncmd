
package com.asyncmd.utils;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;

/**
 * @author wangwendi
 * @version $Id: LocalExceptionUtil.java, v 0.1 2019年07月20日 wangwendi Exp $
 */
public class LocalExceptionUtil {
    protected static ThreadLocal<AsynException> localException = new ThreadLocal<AsynException>();


    public static void set(AsynException asynException){
        localException.set(asynException);
    }


    public static AsynException get() {
        AsynException asynException = localException.get();
        localException.remove();
        return asynException;
    }

}