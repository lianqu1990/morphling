package com.lianqu1990.springboot.web.register.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hanchao
 * @date 2017/9/18 14:18
 */
@Data
@ConfigurationProperties(prefix = "web-register") //cant ends with "."
public class RegisterConfig {

    private String connectString;
    private String [] preferedNetworks;
    private int port;
    private String prefix;
    private boolean registOnStartup = true;
}
