package com.lianqu1990.morphling.service.remote.endpoint;

import com.lianqu1990.morphling.common.consts.EndpointConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author hanchao
 * @date 2017/11/9 11:08
 */
public abstract class AbstractEndpointService<T> implements EndpointService<T>{
    private final ParameterizedTypeReference<T> parameterizedTypeReference;
    public AbstractEndpointService(){
        Type superClass = getClass().getGenericSuperclass();
        final Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        parameterizedTypeReference = new ParameterizedTypeReference<T>() {
            @Override
            public Type getType() {
                return type;
            }
        };
    }

    @Autowired
    protected RestTemplate restTemplate;

    protected HttpMethod httpMethod() {
        return HttpMethod.GET;
    }

    protected HttpEntity httpEntity() {
        return new HttpEntity(new HttpHeaders());
    }

    public T request(String host, int port, String contextPath) {
        String url = EndpointConsts.DEFAULT_SCHEMA + host + ":" + port + contextPath + EndpointConsts.DEFAULT_CONTEXT + endpoint();
        return restTemplate.exchange(url, httpMethod(), httpEntity(), parameterizedTypeReference).getBody();
    }


    public abstract String endpoint();
}
