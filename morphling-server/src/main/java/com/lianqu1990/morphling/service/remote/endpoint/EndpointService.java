package com.lianqu1990.morphling.service.remote.endpoint;

/**
 * @author hanchao
 * @date 2017/12/7 10:38
 */
public interface EndpointService<T> {
    T request(String host, int port, String contextPath);
    String endpoint();
}
