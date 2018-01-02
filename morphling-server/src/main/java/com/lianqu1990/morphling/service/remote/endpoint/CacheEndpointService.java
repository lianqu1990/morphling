package com.lianqu1990.morphling.service.remote.endpoint;

import com.lianqu1990.morphling.common.consts.EndpointConsts;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author hanchao
 * @date 2017/12/19 17:27
 */
@Service
public class CacheEndpointService extends AbstractEndpointService<Map> {
    @Override
    public String endpoint() {
        return EndpointConsts.CACHE;
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }
}
