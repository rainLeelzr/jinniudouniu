package com.jinniu.commonjn.util;

import org.springframework.web.socket.WebSocketSession;

import java.net.InetSocketAddress;

/**
 * 获取ip地址
 */
public class IpAddressUtil {

    public static String getIp(WebSocketSession session) {
        InetSocketAddress remoteAddress = session.getRemoteAddress();
        String ip = remoteAddress.getAddress().toString();
        if (!StringUtil.isBlank(ip)) {
            ip = ip.substring(1, ip.length());
        }
        return ip;
    }
}
