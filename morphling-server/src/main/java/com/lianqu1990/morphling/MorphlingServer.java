package com.lianqu1990.morphling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hanchao
 * @date 2017/10/20 14:04
 */
@ComponentScan(basePackageClasses = MorphlingServer.class)
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@SpringBootConfiguration
public class MorphlingServer {
    public static void main(String[] args){
        SpringApplication.run(MorphlingServer.class, args);
    }
}
