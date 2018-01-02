package com.lianqu1990.springboot.cache.core;

import com.lianqu1990.common.spring.cache.Cached;
import com.lianqu1990.common.spring.cache.CachedInfosBuilder;
import com.lianqu1990.springboot.cache.spel.SpelExecutor;
import com.lianqu1990.springboot.cache.support.web.CacheManageBootEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @author hanchao
 * @date 2017/10/7 14:35
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "htonline.cache-manage.enabled",matchIfMissing = true)
@EnableConfigurationProperties(CacheManageProperties.class)
public class CacheManageAutoConfiguration {

    private CacheManageProperties properties;

    public CacheManageAutoConfiguration(CacheManageProperties properties){
        this.properties = properties;
        if(MapUtils.isEmpty(properties.getPrefix())){
            return;
        }
        for (String key : properties.getPrefix().keySet()) {
            try {
                String value = properties.getPrefix().get(key);
                if(StringUtils.isNotBlank(value)){
                    CacheManageBootEndpoint.prefix.put(Cached.DataScourseType.valueOf(key.toUpperCase()),value);
                }
            } catch(Exception e){
                log.error("",e);
            }
        }
    }

    @Bean
    public CachedInfosBuilder cachedInfosBuilder(){
        return new CachedInfosBuilder(properties.getBasePackage());
    }

    @Bean
    public SpelExecutor spelExecutor(){
        return new SpelExecutor();
    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnClass(AbstractEndpoint.class)
    protected static class WebEnvConfiguration {
        @Bean
        public CacheManageBootEndpoint cacheManageBootEndpoint(){
            return new CacheManageBootEndpoint();
        }
    }

}
