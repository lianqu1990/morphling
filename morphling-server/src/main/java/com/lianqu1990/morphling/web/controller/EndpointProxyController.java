package com.lianqu1990.morphling.web.controller;

import com.lianqu1990.morphling.consts.MorphlingResponse;
import com.lianqu1990.morphling.service.remote.endpoint.EndpointService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hanchao
 * @date 2017/11/9 10:39
 */
@RequestMapping("/endpoint")
@RestController
public class EndpointProxyController {
    @Autowired(required = false)
    private List<EndpointService> endpointServiceList;

    @GetMapping
    public Object handle(String host,int port,String context,String endpoint){
        if(StringUtils.isNotBlank(endpoint) && !endpoint.startsWith("/")){
            endpoint = "/"+endpoint;
        }
        for (EndpointService endpointService : endpointServiceList) {
            if(endpointService.endpoint().equals(endpoint)){
                try {
                    return endpointService.request(host,port,context);
                } catch(Exception e){
                    return MorphlingResponse.CONNECTION_ERROR;
                }
            }
        }
        return null;
    }
}
