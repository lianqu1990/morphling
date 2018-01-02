package com.lianqu1990.springboot.degrade.support;

import com.ctrip.framework.foundation.Foundation;
import com.google.common.collect.Lists;
import com.lianqu1990.springboot.degrade.core.Degrade;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author hanchao
 * @date 2017/9/30 19:54
 */
@ConfigurationProperties(prefix = "endpoints.degrade")
public class DegradeEndpoint extends AbstractEndpoint<DegradeEndpoint.DegradeEndpointBean> implements BeanPostProcessor {
    private String basePackage;//定义筛选的包路径，减少扫描事件
    private boolean exclude;//是否需要筛选
    private DegradeEndpointBean endpointBean = new DegradeEndpointBean();

    public DegradeEndpoint(String basePackage) {
        super("degrade");
        if (!StringUtils.isEmpty(basePackage)) {
            exclude = true;
        }
        this.basePackage = basePackage;
        endpointBean.setAppId(Foundation.app().getAppId());
        endpointBean.setEnv(Foundation.server().getEnvType());
    }

    @Override
    public DegradeEndpointBean invoke() {
        return endpointBean;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        try {
            if (!exclude || bean.getClass().getName().startsWith(basePackage)) {
                Method[] declaredMethods = bean.getClass().getMethods();
                for (Method declaredMethod : declaredMethods) {
                    Degrade annotation = declaredMethod.getAnnotation(Degrade.class);
                    if(annotation != null){
                        endpointBean.addDegradeInfo(build(annotation));
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

    private DegradeInfo build(Degrade degrade){
        return DegradeInfo.builder()
                .key(degrade.key())
                .bizName(degrade.name())
                .build();
    }

    @Data
    static class DegradeEndpointBean {
        private List<DegradeInfo> degradeInfos = Lists.newArrayList();
        private String appId;
        private String env;

        public void addDegradeInfo(DegradeInfo degradeInfo){
            this.degradeInfos.add(degradeInfo);
        }
    }

    @Data
    @Builder
    static class DegradeInfo {
        private String key;
        private String bizName;
    }
}
