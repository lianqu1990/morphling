package com.lianqu1990.common.spring.web;

/**
 * @author hanchao
 * @date 2017/10/5 15:28
 */
public class MediaType extends org.springframework.http.MediaType {
    public final static String CHARSET_SUFFIX = ";charset=UTF-8";
    public final static String APPLICATION_FORM_URLENCODED_UTF8_VALUE = APPLICATION_FORM_URLENCODED_VALUE + CHARSET_SUFFIX;

    public MediaType(String type) {
        super(type);
    }
}
