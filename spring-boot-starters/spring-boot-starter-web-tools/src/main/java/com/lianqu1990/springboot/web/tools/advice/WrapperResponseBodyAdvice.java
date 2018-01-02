package com.lianqu1990.springboot.web.tools.advice;

import com.lianqu1990.common.CommonResult;
import com.lianqu1990.common.Result;
import com.lianqu1990.common.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 如果不是spring boot环境，应该会忽略掉conditional
 * Created by shaojieyue
 * Created time 2016-04-18 09:56
 */

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@ConditionalOnProperty(value = "htonline.wrapper.enabled", havingValue = "true", matchIfMissing = true)
public class WrapperResponseBodyAdvice implements ResponseBodyAdvice {

    @Autowired
    private AdviceExcluder adviceExcluder;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (isHtml(mediaType)) {
            return o;
        }
        //优先返回无需wrapper的，保证效率
        if(o != null && o instanceof Result){
            return o;
        }
        if(adviceExcluder.ignore(o,serverHttpRequest)){
            return o;
        }
        //最后进行包装
        return o == null ? CommonResult.SUCCESS : new SuccessResponse(o);
    }


    /**
     * 返回是否是html
     *
     * @param mediaType
     * @return
     */
    public boolean isHtml(MediaType mediaType) {
        return mediaType.includes(MediaType.TEXT_HTML);
    }





}
