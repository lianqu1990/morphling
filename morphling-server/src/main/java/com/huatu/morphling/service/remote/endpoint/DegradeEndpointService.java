package com.huatu.morphling.service.remote.endpoint;

import com.huatu.morphling.common.bean.degrade.DegradeEndpoint;
import com.huatu.morphling.common.consts.EndpointConsts;
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
