package com.lianqu1990.morphling.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hanchao
 * @date 2017/11/10 12:59
 */
@ComponentScan(basePackageClasses = MorphlingAgent.class)
@EnableAutoConfiguration
@SpringBootConfiguration
public class MorphlingAgent {
    public static void main(String[] args){
        SpringApplication.run(MorphlingAgent.class, args);
    }
}
