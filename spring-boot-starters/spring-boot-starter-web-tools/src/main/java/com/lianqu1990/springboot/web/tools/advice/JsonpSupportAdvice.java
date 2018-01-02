package com.lianqu1990.springboot.web.tools.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * jsonp和hateos不兼容
 * @author hanchao
 * @date 2017/10/19 10:42
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@ConditionalOnProperty(value = "htonline.jsonp.enabled", havingValue = "true", matchIfMissing = true)
@ControllerAdvice
public class JsonpSupportAdvice implements ResponseBodyAdvice<Object> {
    @Autowired
    private AdviceExcluder adviceExcluder;

    /**
     * Pattern for validating jsonp callback parameter values.
     */
    private static final Pattern CALLBACK_PARAM_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");


    private final Log logger = LogFactory.getLog(getClass());

    private final String[] jsonpQueryParamNames;

    public JsonpSupportAdvice(){
        jsonpQueryParamNames = new String []{"callback"};
    }


    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
                                           MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {

        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

        for (String name : this.jsonpQueryParamNames) {
            String value = servletRequest.getParameter(name);
            if (value != null) {
                if (!isValidJsonpQueryParam(value)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Ignoring invalid jsonp parameter value: " + value);
                    }
                    continue;
                }
                MediaType contentTypeToUse = getContentType(contentType, request, response);
                response.getHeaders().setContentType(contentTypeToUse);
                bodyContainer.setJsonpFunction(value);
                break;
            }
        }
    }

    /**
     * Validate the jsonp query parameter value. The default implementation
     * returns true if it consists of digits, letters, or "_" and ".".
     * Invalid parameter values are ignored.
     * @param value the query param value, never {@code null}
     * @since 4.1.8
     */
    protected boolean isValidJsonpQueryParam(String value) {
        return CALLBACK_PARAM_PATTERN.matcher(value).matches();
    }

    /**
     * Return the content type to set the response to.
     * This implementation always returns "application/javascript".
     * @param contentType the content type selected through content negotiation
     * @param request the current request
     * @param response the current response
     * @return the content type to set the response to
     */
    protected MediaType getContentType(MediaType contentType, ServerHttpRequest request, ServerHttpResponse response) {
        return new MediaType("application", "javascript");
    }



    /**
     * Wrap the body in a {@link MappingJacksonValue} value container (for providing
     * additional serialization instructions) or simply cast it if already wrapped.
     */
    protected MappingJacksonValue getOrCreateContainer(Object body) {
        return (body instanceof MappingJacksonValue ? (MappingJacksonValue) body : new MappingJacksonValue(body));
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType contentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(!adviceExcluder.ignore(body,request)){
            MappingJacksonValue container = getOrCreateContainer(body);
            beforeBodyWriteInternal(container, contentType, returnType, request, response);
            return container;
        }
        return body;
    }
}
