/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.model;

import java.util.Comparator;

/**
 * @author wangwendi
 * @version $Id: AsynExecuterComparator.java, v 0.1 2019年07月19日 下午6:14 wangwendi Exp $
 */
public class AsynExecuterComparator implements Comparator<AbstractAsynExecuter> {
    public int compare(AbstractAsynExecuter o1, AbstractAsynExecuter o2) {

        return o2.getSort() - o1.getSort();
    }
}