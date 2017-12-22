package com.huatu.morphling.service.remote.endpoint;

import com.huatu.morphling.common.consts.EndpointConsts;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author hanchao
 * @date 2017/12/7 10:59
 */
@Service
public class InfoEndpointService extends AbstractEndpointService<Map> {
    @Override
    public String endpoint() {
        return EndpointConsts.INFO;
    }
}
