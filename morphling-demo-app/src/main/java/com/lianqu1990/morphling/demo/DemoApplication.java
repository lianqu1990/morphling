package com.lianqu1990.morphling.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hanchao
 * @date 2018/1/12 13:13
 */
@ComponentScan(basePackageClasses = DemoApplication.class)
@EnableAutoConfiguration
@SpringBootConfiguration
public class DemoApplication {
    public static void main(String[] args){
        SpringApplication.run(DemoApplication.class, args);
    }
}
