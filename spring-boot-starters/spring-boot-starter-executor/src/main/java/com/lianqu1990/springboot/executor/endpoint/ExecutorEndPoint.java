package com.lianqu1990.springboot.executor.endpoint;

import com.lianqu1990.springboot.executor.support.NamedThreadPoolTaskExecutor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hanchao
 * @date 2017/9/30 19:54
 */
@ConfigurationProperties(prefix = "endpoints.executor")
public class ExecutorEndPoint extends AbstractEndpoint<Map> implements ApplicationContextAware,InitializingBean {
    private ApplicationContext applicationContext;
    private Map<String,Executor> executors;

    public ExecutorEndPoint() {
        super("executor");
    }

    @Override
    public Map invoke() {
        Map<String,Object> result = new HashMap<>();
        result.put("size",executors == null ? 0 : executors.size());
        if(executors != null){
            executors.forEach((name,executor) -> {
                Map<String,Object> info = new HashMap<>();
                if(executor instanceof NamedThreadPoolTaskExecutor){
                    info.put("name",((NamedThreadPoolTaskExecutor) executor).getName());
                }else{
                    info.put("name",name);
                } //TODO 由spring bean命名或者传入

                if(executor instanceof ThreadPoolExecutor || executor instanceof ThreadPoolTaskExecutor){
                    ThreadPoolExecutor instance;
                    info.put("type",executor.getClass().getCanonicalName());
                    instance = executor instanceof ThreadPoolExecutor ? (ThreadPoolExecutor)executor : ((ThreadPoolTaskExecutor)executor).getThreadPoolExecutor();

                    ExecutorInfo executorInfo = ExecutorInfo.builder()
                            .coreSize(instance.getCorePoolSize())
                            .maxSize(instance.getMaximumPoolSize())
                            .active(instance.getActiveCount())
                            .taskCount(instance.getTaskCount())
                            .completedTaskCount(instance.getCompletedTaskCount())
                            .largestPoolSize(instance.getLargestPoolSize())
                            .queueSize(instance.getQueue().size())
                            .queueClass(instance.getQueue().getClass().getCanonicalName())
                            .build()
                            .setStatus(instance.isTerminated());

                    info.put("info",executorInfo);

                }else{
                    info.put("type","unknown");
                }

                result.put(name,info);
            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executors = applicationContext.getBeansOfType(Executor.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
