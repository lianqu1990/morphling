package com.lianqu1990.springboot.restclient.support;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hanchao
 * @date 2017/8/29 11:53
 */
@ConfigurationProperties(prefix = "rest-clients")
public class RestClientConfig {
    private static int DEFAULT_CONNECT_TIMEOUT = 2000;
    private static int DEFAULT_CONNECT_REQUEST_TIMEOUT = 2000;
    private static int DEFAULT_READ_TIMEOUT = 5000;
    private static int DEFAULT_WRITE_TIMEOUT = 2000;
    private static int DEFAULT_SOCKET_TIMEOUT = DEFAULT_READ_TIMEOUT + DEFAULT_WRITE_TIMEOUT;
    private int DEFAULT_KEEPALIVE_TIME = 5 * 60 * 1000; //五分钟

    private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private int connectRequestTimeout = DEFAULT_CONNECT_REQUEST_TIMEOUT;
    private int readTimeout = DEFAULT_READ_TIMEOUT;
    private int writeTimeout = DEFAULT_WRITE_TIMEOUT;
    private int socketTimeout = DEFAULT_SOCKET_TIMEOUT;
    private int keepAliveTime = DEFAULT_KEEPALIVE_TIME;
    private int maxTotal = 100;
    private int maxPerRoute = 100;


    private boolean redirectable = true;//是否需要重定向，默认为true

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getConnectRequestTimeout() {
        return connectRequestTimeout;
    }

    public void setConnectRequestTimeout(int connectRequestTimeout) {
        this.connectRequestTimeout = connectRequestTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public boolean isRedirectable() {
        return redirectable;
    }

    public void setRedirectable(boolean redirectable) {
        this.redirectable = redirectable;
    }
}
