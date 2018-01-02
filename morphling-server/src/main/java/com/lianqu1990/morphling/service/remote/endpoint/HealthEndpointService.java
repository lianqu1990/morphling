package com.lianqu1990.morphling.service.remote.endpoint;

import com.lianqu1990.morphling.common.consts.EndpointConsts;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author hanchao
 * @date 2017/11/9 10:40
 */
@Service
public class HealthEndpointService extends AbstractEndpointService<Map>{

    @Override
    public String endpoint() {
        return EndpointConsts.HEALTH;
    }
}
