package com.cmos.audiotransfer.common.util;

import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by hejie
 * Date: 18-7-13
 * Time: 上午10:08
 * Description:
 */
public class LocalHostInfo {

    public static String ipAddr;

    public static String hostName;

    public static String localInfo;

    public void init() throws UnknownHostException {
        this.ipAddr = InetAddress.getLocalHost().getHostAddress();
        this.hostName = InetAddress.getLocalHost().getHostName();
        this.localInfo = new StringBuilder(hostName).append(":").append(hostName).toString();
    }

    public static String getIpAddr() {
        return ipAddr;
    }

    public static void setIpAddr(String ipAddr) {
        LocalHostInfo.ipAddr = ipAddr;
    }

    public static String getHostName() {
        return hostName;
    }

    public static void setHostName(String hostName) {
        LocalHostInfo.hostName = hostName;
    }

    public static String getLocalInfo() {
        return localInfo;
    }

    public static void setLocalInfo(String localInfo) {
        LocalHostInfo.localInfo = localInfo;
    }
}
