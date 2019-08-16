
package com.asyncmd.utils;

/**
 * 分页工具类
 * @author wangwendi
 * @version $Id: SubTableUtil.java, v 0.1 2019年07月22日 wangwendi Exp $
 */
public class SubTableUtil {
    /**
     * 分表数量
     */
    private static int tableNum;

    /**
     * 获取表名
     * @param tableIndex
     * @param bizId
     * @param tableName
     * @return
     */
    public static String getTableName(Integer tableIndex,String bizId,String tableName){
        if (tableNum > 0){
            return tableName + getSubTableIndex(tableIndex,bizId);
        }
        return tableName;

    }

    /**
     * 获取分表位置
     * @param tableIndex
     * @param bizId
     * @return
     */
    private static String getSubTableIndex(Integer tableIndex,String bizId){
        if (tableIndex == null){
            return getTableIndex(getIndex(tableNum,bizId));
        }
        return getTableIndex(tableIndex);
    }

    private static String getTableIndex(int tableIndex){
        if (tableIndex < 10){
            return "0" + tableIndex;
        }
        return String.valueOf(tableIndex);
    }


    /**
     * 根据业务id计算分配到哪个表中
     * @param tableNum 分表数量
     * @param bizId 业务id
     * @return
     */
    public static int getIndex(Integer tableNum,String bizId){
        return (tableNum - 1) & hash(bizId);
    }

    private static int hash(String bizId){
        int h;
        return (h = bizId.hashCode()) ^ (h >>> 16);
    }

    public static void init(Integer num) {
        tableNum = num;
    }
}