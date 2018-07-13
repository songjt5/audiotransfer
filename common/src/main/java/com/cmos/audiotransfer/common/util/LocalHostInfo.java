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

    String ipAddr;

    String hostName;

    String localInfo;

    public LocalHostInfo() throws UnknownHostException {
        this.ipAddr = InetAddress.getLocalHost().getHostAddress();
        this.hostName = InetAddress.getLocalHost().getHostName();
        this.localInfo = new StringBuilder(hostName).append(":").append(hostName).toString();
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getLocalInfo() {
        return localInfo;
    }

    public void setLocalInfo(String localInfo) {
        this.localInfo = localInfo;
    }
}
