package com.lianqu1990.common.spring.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.lang.reflect.Constructor;
import java.util.concurrent.Executor;

/**
 * 发布事件工具类
 * @author hanchao
 * @date 2017/10/13 13:27
 */
@Slf4j
public class EventPublisher implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private Executor executor;

    public EventPublisher(){
        this(new SimpleAsyncTaskExecutor());
    }

    public EventPublisher(Executor executor){
        this.executor = executor;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public <T extends ApplicationEvent> T buildEvent(Class<T> clazz,Object source){
        try {
            Constructor<T> constructor = clazz.getConstructor(Object.class);
            return constructor.newInstance(source);
        } catch(Exception e){
            log.error("error get event instance...",e);
        }
        return null;
    }

    public void publishEvent(ApplicationEvent event){
        applicationContext.publishEvent(event);
    }

    public boolean publishEvent(Class<? extends ApplicationEvent> event,Object source){
        return publishEvent(event,source,null);
    }

    /**
     * @param clazz
     */
    public <T extends ApplicationEvent> boolean publishEvent(Class<T> clazz,Object source,EventInitializer<T> initializer){
        ApplicationEvent event = buildEvent(clazz,source);
        if(event == null){
            return false;
        }
        if(initializer != null){
            initializer.initializeEvent((T) event);
        }
        applicationContext.publishEvent(event);
        return true;
    }

    public void publishEventAsync(ApplicationEvent event){
        executor.execute(() -> publishEvent(event));
    }

    public void publishEventAsync(Class<? extends ApplicationEvent> event,Object source){
        publishEventAsync(event,source,null);
    }

    public <T extends ApplicationEvent> void publishEventAsync(Class<T> clazz,Object source,EventInitializer<T> initializer){
        executor.execute( () -> publishEvent(clazz,source,initializer));
    }

    public interface EventInitializer<T extends ApplicationEvent> {
        void initializeEvent(T event);
    }
}
