package com.lianqu1990.springboot.web.register.core;

import com.google.common.primitives.Ints;
import com.lianqu1990.common.utils.env.IpAddrUtil;
import com.lianqu1990.springboot.web.register.WebRegister;
import com.lianqu1990.springboot.web.register.etcd.EtcdWebRegister;
import com.lianqu1990.springboot.web.register.listener.WebRegistListener;
import com.lianqu1990.springboot.web.register.listener.WebRegisterEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Optional;

/**
 * @author hanchao
 * @date 2017/9/18 14:39
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(RegisterConfig.class)
@ConditionalOnMissingClass("org.springframework.boot.test.context.SpringBootTest") //测试环境下不允许运行
public class RegisterAutoConfiguration {
    private RegisterConfig registerConfig;
    @Autowired
    private Environment env;

    public RegisterAutoConfiguration(RegisterConfig registerConfig){
        this.registerConfig = registerConfig;
    }

    @ConditionalOnMissingBean(WebRegister.class)
    @Bean
    public EtcdWebRegister etcdWebRegister(){
        log.info("begin build etcd register...");
        if(StringUtils.isBlank(registerConfig.getConnectString())){
            log.error("unknown regist center,wont regist....");
            return null;
        }

        String appName = env.getProperty("spring.application.name");
        int port = Optional.ofNullable(Ints.tryParse(env.getProperty("server.port"))).orElse(0);
        if(registerConfig.getPort() > 0){
            //自己指定的注册端口，例如docker映射
            port = registerConfig.getPort();
        }
        String host = IpAddrUtil.getLocalIP(registerConfig.getPreferedNetworks());
        if(StringUtils.isBlank(appName) || StringUtils.isBlank(host)){
            log.error("unknown host or appName,wont regist....");
            return null;
        }
        if(port < 0 || port > 65535){
            log.error("illegal port number,wont regist....");
        }
        return new EtcdWebRegister(registerConfig.getConnectString(),host,port,appName,registerConfig.getPrefix());
    }


    @Bean
    @ConditionalOnBean(WebRegister.class)
    @ConditionalOnWebApplication
    public WebRegistListener webRegistListener(@Autowired WebRegister webRegister){
        return new WebRegistListener(webRegister,registerConfig.isRegistOnStartup());
    }

    @Bean
    @ConditionalOnBean(WebRegister.class)
    @ConditionalOnWebApplication
    public WebRegisterEndpoint registerController(){
        return new WebRegisterEndpoint();
    }
}
