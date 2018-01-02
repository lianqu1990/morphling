package com.lianqu1990.springboot.executor.support;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author hanchao
 * @date 2017/9/30 20:15
 */
public class NamedThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    private static final long serialVersionUID = 1L;
    private String name;

    public NamedThreadPoolTaskExecutor(String name){
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
