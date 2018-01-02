package com.lianqu1990.springboot.degrade;

import com.lianqu1990.springboot.degrade.support.DegradeAspect;
import com.lianqu1990.springboot.degrade.support.DegradeEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author hanchao
 * @date 2017/10/16 21:54
 */
@Configuration
@ConditionalOnProperty(name = "htonline.degrade.enabled",matchIfMissing = true)
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class DegradeAutoConfiguration {

    @Bean
    public DegradeAspect degradeAspect(){
        return new DegradeAspect();
    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnClass(AbstractEndpoint.class)
    protected static class EndpointConfiguration {
        @Value("${htonline.degrade.base-package:}")
        private String basePackage;
        @Bean
        public DegradeEndpoint degradeEndpoint(){
            return new DegradeEndpoint(basePackage);
        }
    }
}
