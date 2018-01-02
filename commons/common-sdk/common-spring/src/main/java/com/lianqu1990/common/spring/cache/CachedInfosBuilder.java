package com.lianqu1990.common.spring.cache;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author hanchao
 * @date 2017/10/6 9:57
 */
public class CachedInfosBuilder implements BeanPostProcessor {
    private String basePackage;//定义筛选的包路径，减少扫描事件
    private boolean exclude;//是否需要筛选

    public CachedInfosBuilder() {
        this("");
    }

    public CachedInfosBuilder(String basePackage) {
        if (!StringUtils.isEmpty(basePackage)) {
            exclude = true;
        }
        this.basePackage = basePackage;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        try {
            if (!exclude || bean.getClass().getName().startsWith(basePackage)) {
                Method[] declaredMethods = bean.getClass().getMethods();
                for (Method declaredMethod : declaredMethods) {
                    Cached annotation = declaredMethod.getAnnotation(Cached.class);
                    if(annotation != null){
                        CachedInfoHolder.addCache(annotation,bean);
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
