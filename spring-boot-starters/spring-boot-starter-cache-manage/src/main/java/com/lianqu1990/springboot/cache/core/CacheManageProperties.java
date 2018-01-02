package com.lianqu1990.springboot.cache.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author hanchao
 * @date 2017/10/8 10:17
 */
@ConfigurationProperties(prefix = "htonline.cache-manage")
@Data
public class CacheManageProperties {
    private String basePackage;

    private Map<String,String> prefix;
}
