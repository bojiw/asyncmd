/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.utils;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;

/**
 * @author wangwendi
 * @version $Id: ValidationUtil.java, v 0.1 2019年07月12日 上午10:00 wangwendi Exp $
 */
public class ValidationUtil {

    public static void isNull(Object object, AsynExCode code){
        if (object == null){
            throw new AsynException(code);
        }

    }

}