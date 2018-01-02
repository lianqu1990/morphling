package com.lianqu1990.springboot.degrade.support;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.lianqu1990.springboot.degrade.core.Degrade;
import com.lianqu1990.springboot.degrade.core.DegradeConsts;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author hanchao
 * @date 2017/10/16 17:10
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Aspect
public class DegradeAspect {
    @ApolloConfig("degrade")
    private Config config;


    @Pointcut("execution(* *..*(..)) && @annotation(com.lianqu1990.springboot.degrade.core.Degrade)")
    private void degradeMethod() {
    }


    @Around("degradeMethod()")
    public Object doArround(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Object target = pjp.getTarget();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        Degrade degrade = method.getAnnotation(Degrade.class);
        String switchStatus = config.getProperty(degrade.key(), DegradeConsts.CLOSED);
        if (! DegradeConsts.OPEN.equals(switchStatus)) {
            return pjp.proceed();
        }
        String degradeMethod = degrade.method();
        if(StringUtils.isEmpty(degradeMethod)){
            degradeMethod = method.getName() + DegradeConsts.METHOD_SUFFIX;
        }
        try {
            Method degradeMethodInvoke = method.getDeclaringClass().getMethod(degradeMethod,method.getParameterTypes());
            return degradeMethodInvoke.invoke(target,args);
        } catch(NoSuchMethodException | SecurityException e){
            return handleAutoDegrade(method);
        }
    }

    private Object handleAutoDegrade(Method method){
        Class<?> returnType = method.getReturnType();
        if(! returnType.isPrimitive()){
            return null;
        }
        if(returnType.equals(Void.TYPE)){
            return null;
        }
        if(returnType.equals(Boolean.TYPE)){
            return false;
        }
        if(returnType.equals(Character.TYPE)){
            return ' ';
        }
        return 0;
    }
}
