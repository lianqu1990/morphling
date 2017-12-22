package com.huatu.morphling.service.remote.endpoint;

import com.huatu.morphling.common.consts.EndpointConsts;
import org.springframework.stereotype.Service;

/**
 * @author hanchao
 * @date 2017/12/7 15:20
 */
@Service
public class WebRegisterEndPointService extends AbstractEndpointService<Integer> {
    @Override
    public String endpoint() {
        return EndpointConsts.WEB_REGISTER;
    }
}
