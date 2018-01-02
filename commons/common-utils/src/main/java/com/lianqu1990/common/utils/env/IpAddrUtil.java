package com.lianqu1990.common.utils.env;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IpAddrUtil {
    private static final Logger log = LoggerFactory.getLogger(IpAddrUtil.class);

    /**
     * 判断当前操作是否Windows.
     *
     * @return true---是Windows操作系统
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    public static List<String> getLocalIPs() {
        InetAddress ip = null;
        List<String> innerIpList = new ArrayList<String>();
        try {
            // 如果是Windows操作系统
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost();
                innerIpList.add(ip.toString());
            }
            Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                if (ni.isVirtual() || ni.isLoopback()) {
                    continue;
                }
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = (InetAddress) ips.nextElement();
                    if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
                            && ip.getHostAddress().indexOf(":") == -1) {
                        innerIpList.add(ip.getHostAddress());
                    }
                }

            }
        } catch (Exception e) {
            log.error("get local ip error:", e);
        }
        return innerIpList;
    }

    /**
     * 获取本机IP地址，并自动区分Windows还是Linux操作系统
     *
     * @return String
     */
    public static String getLocalIP() {
        List<String> ips = getLocalIPs();
        if (ips.size() > 0) {
            return ips.get(ips.size() - 1);
        } else {
            return "";
        }
    }

    public static String getLocalIP(String[] preferedNetworks){
        if(ArrayUtils.isEmpty(preferedNetworks)){
            return getLocalIP();
        }
        List<String> ips = getLocalIPs();
        if(ips.size() > 0){
            for (String ip : ips) {
                for (String preferedNetwork : preferedNetworks) {
                    if(preferedNetwork == null || ip.matches(preferedNetwork) || ip.startsWith(preferedNetwork)){
                        return ip;
                    }
                }
            }
        }
        return "";
    }


    public static Long getIpFlag(int flag) {
        String ip = IpAddrUtil.getLocalIP();
        log.info("ip=" + ip);
        String ips[] = ip.split("[.]");
        return Long.valueOf(ips[flag - 1]);
    }
}
