package com.lianqu1990.morphling.spring.resolver;

import com.lianqu1990.common.exception.UnauthorizedException;
import com.lianqu1990.morphling.bean.UserInfo;
import com.lianqu1990.morphling.consts.MorphlingResponse;
import com.lianqu1990.morphling.spring.security.SecurityUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author hanchao
 * @date 2017/8/24 19:45
 */
@Component
public class UserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.hasParameterAnnotation(UserSession.class) && isLegalType(parameter.getParameterType()));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        UserSession config = parameter.getParameterAnnotation(UserSession.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = null;
        if (authentication.getPrincipal() instanceof SecurityUser) {
            userInfo = getAccount(authentication);
        }
        if(userInfo == null && config.check()){
            throw new UnauthorizedException(MorphlingResponse.UNAUTHORIZED);
        }
        return userInfo;
    }

    /**
     * 返回给用户的account信息
     * @param authentication
     * @return
     */
    private UserInfo getAccount(Authentication authentication){
        return ((SecurityUser) authentication.getPrincipal()).getUserInfo();
    }

    /**
     * 是否合法的参数类型
     * @param clazz
     * @return
     */
    private boolean isLegalType(Class<?> clazz){
        if(UserInfo.class.isAssignableFrom(clazz)){
            return true;
        }
        return false;
    }



}
