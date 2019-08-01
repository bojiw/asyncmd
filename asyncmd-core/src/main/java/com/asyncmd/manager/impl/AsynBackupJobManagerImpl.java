
package com.asyncmd.manager.impl;

import com.asyncmd.config.AsynGroupConfig;
import com.asyncmd.enums.AsynStatus;
import com.asyncmd.manager.AsynBackupJobManager;
import com.asyncmd.model.AsynCmd;
import com.asyncmd.service.AsynExecuterService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author wangwendi
 * @version $Id: AsynBackupJobManagerImpl.java, v 0.1 2019年07月23日  wangwendi Exp $
 */
@Service
public class AsynBackupJobManagerImpl implements AsynBackupJobManager {
    private static Log log = LogFactory.getLog(AsynBackupJobManagerImpl.class);


    @Autowired
    private AsynExecuterService asynExecuterService;
    @Autowired
    private AsynGroupConfig asynGroupConfig;

    @Override
    public void backup(Integer tableIndex) {
        Integer maxNo = asynGroupConfig.getAsynConfig().getAsynJobConfig().getAsynBackupConfig().getMaxNo();
        if (maxNo == null){
            return;
        }
        for (int i=0;i < maxNo;i++){
            List<AsynCmd> asynCmdList = asynExecuterService.queryAsynCmd(1000, tableIndex, AsynStatus.SUCCESS, getWhereTime());
            if (CollectionUtils.isEmpty(asynCmdList)){
                return;
            }
            try {
                asynExecuterService.backupCmd(tableIndex,asynCmdList);
            }catch (Exception e){
                log.error("异步命令备份异常" + e.getMessage(),e);
            }

        }

    }

    /**
     * 获取打算捞取多久以前的数据 默认30天
     * @return
     */
    private Date getWhereTime(){
        Calendar calendar = Calendar.getInstance();
        Integer beforeDate = asynGroupConfig.getAsynConfig().getAsynJobConfig().getAsynBackupConfig().getBeforeDate();
        calendar.add(Calendar.DAY_OF_MONTH, -beforeDate);
        return calendar.getTime();
    }

}