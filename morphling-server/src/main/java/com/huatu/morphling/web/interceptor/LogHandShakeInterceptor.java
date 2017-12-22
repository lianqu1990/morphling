package com.huatu.morphling.web.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

import static com.huatu.morphling.common.consts.WebParamConsts.LOGID_PARAM;

/**
 * @author hanchao
 * @date 2017/11/20 13:17
 */
public class LogHandShakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(request instanceof ServletServerHttpRequest){
            String logId = ((ServletServerHttpRequest) request).getServletRequest().getParameter(LOGID_PARAM);
            if(StringUtils.isNotBlank(logId)){
                attributes.put(LOGID_PARAM,logId);
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
