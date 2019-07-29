
package com.asyncmd.utils;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;

/**
 * @author wangwendi
 * @version $Id: ValidationUtil.java, v 0.1 2019年07月12日 wangwendi Exp $
 */
public class ValidationUtil {

    public static void isNull(Object object, AsynExCode code){
        if (object == null){
            throw new AsynException(code);
        }

    }

}