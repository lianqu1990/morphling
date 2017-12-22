package com.huatu.morphling.spring.conf.web;

import com.huatu.morphling.web.interceptor.LogHandShakeInterceptor;
import com.huatu.morphling.web.socket.EchoHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author hanchao
 * @date 2017/11/20 11:23
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

    @Autowired
    private EchoHandler echoHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(echoHandler,"/echo")
                .setAllowedOrigins("*")
                .addInterceptors(new LogHandShakeInterceptor());
    }
}
