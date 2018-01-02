package com.lianqu1990.springboot.web.tools.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理异常到响应
 * @author hanchao
 * @date 2017/4/9 18:41
 */
public interface ErrorResultHandler {
    ModelAndView handle( HttpServletRequest request, HandlerMethod handlerMethod, Object errorResult, HttpStatus status);
}
