package com.lianqu1990.morphling.service.remote.endpoint;

import com.lianqu1990.morphling.common.bean.degrade.DegradeEndpoint;
import com.lianqu1990.morphling.common.consts.EndpointConsts;
import org.springframework.stereotype.Service;

/**
 * @author hanchao
 * @date 2017/12/21 14:49
 */
@Service
public class DegradeEndpointService extends AbstractEndpointService<DegradeEndpoint> {
    @Override
    public String endpoint() {
        return EndpointConsts.DEGRADE;
    }
}
