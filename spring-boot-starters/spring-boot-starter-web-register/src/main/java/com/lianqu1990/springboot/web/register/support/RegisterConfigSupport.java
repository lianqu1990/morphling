package com.lianqu1990.springboot.web.register.support;

import com.lianqu1990.springboot.web.register.core.RegisterConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.beans.PropertyDescriptor;

/**
 * @author hanchao
 * @date 2017/9/18 14:16
 */
@Deprecated
public class RegisterConfigSupport implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(EnableWebRegister.class.getName()));
        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(RegisterConfig.class);
        for (PropertyDescriptor propertyDescriptor : BeanUtils.getPropertyDescriptors(RegisterConfig.class)) {
            if(propertyDescriptor.getName().equals("class")){
                continue;
            }
            definitionBuilder.addPropertyValue(propertyDescriptor.getName(),attributes.get(propertyDescriptor.getName()));
        }
        AbstractBeanDefinition definition = definitionBuilder.getBeanDefinition();
        registry.registerBeanDefinition(BeanDefinitionReaderUtils.generateBeanName(definition,registry),definition);
    }
}
