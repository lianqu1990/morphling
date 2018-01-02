package com.lianqu1990.springboot.executor;

import com.lianqu1990.springboot.executor.endpoint.ExecutorEndPoint;
import com.lianqu1990.springboot.executor.support.DefaultAsyncUncaughtExceptionHandler;
import com.lianqu1990.springboot.executor.support.ExecutorProperties;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author hanchao
 * @date 2017/9/12 20:46
 */
@ConditionalOnClass(ThreadPoolTaskExecutor.class)
@ConditionalOnProperty(value = "htonline.executor.enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties(ExecutorProperties.class)
public class ExecutorAutoConfiguration {
    private final ExecutorProperties executorProperties;

    public ExecutorAutoConfiguration(ExecutorProperties executorProperties) {
        this.executorProperties = executorProperties;
    }

    @Bean
    public ThreadPoolTaskExecutor coreThreadPool() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(executorProperties.getCorePoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(executorProperties.getMaxPoolSize());
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(executorProperties.isAllowCoreThreadTimeOut());
        threadPoolTaskExecutor.setKeepAliveSeconds(executorProperties.getKeepAliveSeconds());
        threadPoolTaskExecutor.setQueueCapacity(executorProperties.getQueueCapacity());
        return threadPoolTaskExecutor;
    }


    @Bean
    @ConditionalOnClass(SimpleAsyncUncaughtExceptionHandler.class)
    public DefaultAsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler() {
        return new DefaultAsyncUncaughtExceptionHandler();
    }

    @ConditionalOnClass(AbstractEndpoint.class)
    public static class ExecutorMonitorAutoConfiguration {
        @Bean
        public ExecutorEndPoint executorEndPoint() {
            return new ExecutorEndPoint();
        }
    }

    // TODO
    // 多线程池集合，可用来做不同业务间的线程池隔离，异步request，rxjava等，以及监控
    // 线程池命名以及根据命名获取注入
}
