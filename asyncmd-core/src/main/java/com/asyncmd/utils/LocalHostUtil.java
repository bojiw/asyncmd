package com.asyncmd.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author wangwendi
 * @version $Id: LocalHostUtil.java, v 0.1 2019年07月22日 wangwendi Exp $
 */
public class LocalHostUtil {
    private static Log log = LogFactory.getLog(LocalHostUtil.class);


    private static String hostName;
    private static String ip;

    public static String getHostName() {
        return hostName;
    }

    public static String getIp() {
        return ip;
    }

    public static void init(){
        try {
            //服务器网卡有可能有多个 设置了多个IP 因为不知道准确获取哪个 所以把所有正常都ipv4ip都获取拼接
            StringBuffer ipBuffer = new StringBuffer();
            Enumeration nics = NetworkInterface.getNetworkInterfaces();
            while (nics.hasMoreElements()){
                NetworkInterface ifc = (NetworkInterface)nics.nextElement();
                if (!ifc.isUp()){
                    continue;
                }
                Enumeration<InetAddress> inetAddresses = ifc.getInetAddresses();
                while (inetAddresses.hasMoreElements()){
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress() &&
                    inetAddress.getHostAddress().indexOf(":") == -1) {
                        ipBuffer.append(inetAddress.getHostAddress()).append("|");
                    }
                }

            }
            InetAddress address = InetAddress.getLocalHost();

            if (ipBuffer.length() > 0){
                ip = ipBuffer.toString();
            }else {
                ip = address.getHostAddress();
            }
            hostName = address.getHostName();
        }catch (Exception e){
            log.error("本地主机信息初始化异常");

        }

    }

    public static void main(String[] args) {
        LocalHostUtil.init();
        System.out.println(LocalHostUtil.getIp());
        System.out.println(LocalHostUtil.getHostName());
    }
}