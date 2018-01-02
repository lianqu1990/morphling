package com.lianqu1990.springboot.web.tools.exception;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 所有方法可被重写，重新注入到GlobalExceptionhandler
 * @author hanchao
 * @date 2017/4/9 18:43
 */
@Slf4j
public class SimpleErrorResultHandler implements ErrorResultHandler {
    private static final MediaType DEFAULT_MEDIATYPE = MediaType.APPLICATION_JSON;
    private static final String TRACE_PROPERTY_NAME = "trace";

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public ModelAndView handle(HttpServletRequest request,HandlerMethod handlerMethod, Object errorResult, HttpStatus status) {
        MediaType type = getProduceType(request,handlerMethod);
        ModelAndView modelAndView = produce(request,type,errorResult,status);
        if(modelAndView.getStatus() == null){
            modelAndView.setStatus(status == null ? HttpStatus.OK : status);
        }
        //适配spring boot error controller
        request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE,(status == null ? HttpStatus.OK : status).value());
        return modelAndView;
    }

    /**
     * 1,json 2,html
     * @param handlerMethod
     * @param request
     * @return
     */
    public MediaType getProduceType(HttpServletRequest request,HandlerMethod handlerMethod){
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        if(accept != null){
            return contentType2MediaType(accept);
        }
        if(handlerMethod == null){
            return DEFAULT_MEDIATYPE;
        }
        final RequestMapping requestMapping = handlerMethod.getMethodAnnotation(RequestMapping.class);
        if (requestMapping != null &&  requestMapping.produces()!= null && requestMapping.produces().length>0) {//处理RequestMapping 含有指定输出类型
            return contentType2MediaType(requestMapping.produces()[0]);
        }
        //其次检查controller的RequestMapping
        final RequestMapping classRequestMapping = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequestMapping.class);
        if (classRequestMapping != null &&  classRequestMapping.produces()!= null && classRequestMapping.produces().length>0) {//处理RequestMapping 含有指定输出类型
            return contentType2MediaType(classRequestMapping.produces()[0]);
        }
        final ResponseBody responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class);
        if (responseBody != null) {//存在注解ResponseBody,则认为返回json
            return MediaType.APPLICATION_JSON;
        }
        final RestController restController = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RestController.class);
        if(restController != null){
            return MediaType.APPLICATION_JSON;
        }
        return DEFAULT_MEDIATYPE;
    }

    protected MediaType contentType2MediaType(String contentType){
        if(contentType.startsWith(MediaType.TEXT_HTML_VALUE)){
            return MediaType.TEXT_HTML;
        }
        if(contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)){
            return MediaType.APPLICATION_JSON;
        }
        if(contentType.startsWith(MediaType.APPLICATION_XML_VALUE)){
            return MediaType.APPLICATION_XML;
        }
        return DEFAULT_MEDIATYPE;
    }


    public ModelAndView produce(HttpServletRequest request,MediaType type,Object errorResult,HttpStatus status){
        if(type == MediaType.TEXT_HTML){
            return produceHtml(request,errorResult,status);
        }else if(type == MediaType.APPLICATION_JSON){
            return produceJson(request,errorResult,status);
        }else if(type == MediaType.APPLICATION_XML){
            return produceXml(request,errorResult,status);
        }
        throw new IllegalArgumentException("unknown media type to produce...");
    }

    protected ModelAndView produceJson(HttpServletRequest request,Object errorResult,HttpStatus status) {
        final MappingJackson2JsonView jackson2JsonView = new MappingJackson2JsonView();
        jackson2JsonView.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        ModelAndView modelAndView = new ModelAndView(jackson2JsonView,getModel(request,errorResult));
        return modelAndView;
    }

    protected ModelAndView produceXml(HttpServletRequest request,Object errorResult,HttpStatus status) {
        final MappingJackson2XmlView jackson2XmlView = new MappingJackson2XmlView();
        jackson2XmlView.setContentType(MediaType.APPLICATION_XML_VALUE+";charset=UTF-8");
        ModelAndView modelAndView = new ModelAndView(jackson2XmlView,getModel(request,errorResult));
        return modelAndView;
    }

    protected ModelAndView produceHtml(HttpServletRequest request,Object errorResult,HttpStatus status) {
        Map model = getModel(request, errorResult);
        if(model.get(TRACE_PROPERTY_NAME) != null && model.get(TRACE_PROPERTY_NAME) instanceof String){
            model.put(TRACE_PROPERTY_NAME,((String) model.get(TRACE_PROPERTY_NAME)).replace("\r\n","<br/>"));
        }
        return new ModelAndView("error",model);
    }

    protected Map getModel(HttpServletRequest request,Object errorResult){
        Map model = (Map) JSON.toJSON(errorResult);
        if(isIncludeStackTrace(request,MediaType.APPLICATION_JSON_UTF8)){
            Throwable error = errorAttributes.getError(new ServletRequestAttributes(request));
            model.put(TRACE_PROPERTY_NAME, ExceptionUtils.getStackTrace(error));
        }
        return model;
    }

    protected boolean isIncludeStackTrace(HttpServletRequest request,
                                          MediaType produces) {
        ErrorProperties.IncludeStacktrace include = serverProperties.getError().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            String parameter = request.getParameter("trace");
            if (parameter == null) {
                return false;
            }
            return !"false".equals(parameter.toLowerCase());
        }
        return false;
    }


}
