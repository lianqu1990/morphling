package com.lianqu1990.springboot.web.tools.exception;

import com.lianqu1990.common.ErrorResult;
import org.springframework.http.HttpStatus;

/**
 * 将ex转换为errorresult
 * 不要直接处理基类异常，会导致所有的默认子类异常处理失效
 * @author hanchao
 * @date 2017/9/19 16:41
 */
public interface ExceptionResolver {
    /**
     * 处理结果
     * @param ex
     * @return
     */
    ErrorResult resolve(Exception ex);

    /**
     * 是否可以处理
     * @param ex
     * @return
     */
    boolean canResolve(Exception ex);

    /**
     * 方便在spring cloud的异常追踪中，可以追踪特别status
     * @param ex
     * @return
     */
    default HttpStatus status(Exception ex){
        return null;
    }
}
