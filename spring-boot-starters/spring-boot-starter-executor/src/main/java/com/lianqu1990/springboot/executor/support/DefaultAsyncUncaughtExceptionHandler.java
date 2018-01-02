package com.lianqu1990.springboot.executor.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * @author hanchao
 * @date 2017/9/12 21:39
 */
@Slf4j
public class DefaultAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        if (log.isErrorEnabled()) {
            log.error(String.format("Unexpected error occurred invoking async " +
                    "method '%s'.", method), ex);
        }
    }
}
