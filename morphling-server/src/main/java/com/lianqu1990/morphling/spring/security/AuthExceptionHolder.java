package com.lianqu1990.morphling.spring.security;

import org.springframework.security.core.AuthenticationException;

/**
 * 统一返回格式，filter不验证错误
 * @auther hanchao
 * @date 2016/12/8 23:38
 */
public class AuthExceptionHolder {
    private static final ThreadLocal<AuthenticationException> security_exception = new ThreadLocal<AuthenticationException>();

    public static void set(AuthenticationException e) {
        security_exception.set(e);
    }

    public static AuthenticationException get() {
        return security_exception.get();
    }

    public static void clear() {
        security_exception.remove();
    }


}
