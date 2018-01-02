package com.lianqu1990.morphling.spring.security;

import org.springframework.security.core.AuthenticationException;

/**
 * @author hanchao
 * @date 2017/11/7 17:40
 */
public class CaptchaInvalidException extends AuthenticationException {
    public CaptchaInvalidException(String msg) {
        super(msg);
    }
}
