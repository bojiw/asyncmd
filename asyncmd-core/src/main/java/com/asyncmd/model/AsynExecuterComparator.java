
package com.asyncmd.model;

import java.util.Comparator;

/**
 * @author wangwendi
 * @version $Id: AsynExecuterComparator.java, v 0.1 2019年07月19日 wangwendi Exp $
 */
public class AsynExecuterComparator implements Comparator<AbstractAsynExecuter> {

    @Override
    public int compare(AbstractAsynExecuter o1, AbstractAsynExecuter o2) {

        return o2.getSort() - o1.getSort();
    }
}