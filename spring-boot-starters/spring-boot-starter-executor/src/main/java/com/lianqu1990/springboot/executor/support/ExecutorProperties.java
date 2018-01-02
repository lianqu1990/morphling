package com.lianqu1990.springboot.executor.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hanchao
 * @date 2016/11/30 13:34
 */
@ConfigurationProperties(prefix = "executor")
@Data
public class ExecutorProperties {
    private int corePoolSize = 1;
    private int keepAliveSeconds = 60;
    private int maxPoolSize = 10;
    private int queueCapacity = Integer.MAX_VALUE;
    private boolean allowCoreThreadTimeOut = false;
}