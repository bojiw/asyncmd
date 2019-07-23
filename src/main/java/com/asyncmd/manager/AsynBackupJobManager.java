/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.asyncmd.manager;

/**
 * @author wangwendi
 * @version $Id: AsynBackupJobManager.java, v 0.1 2019年07月23日 下午12:19 wangwendi Exp $
 */
public interface AsynBackupJobManager {

    void backup(Integer tableIndex);
}