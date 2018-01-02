package com.lianqu1990.common.utils.web;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import com.lianqu1990.common.utils.encode.CharsetConsts;
import com.lianqu1990.common.utils.encrypt.SignUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * @author hanchao
 * @date 2017/2/12 17:57
 */
public class RequestUtil {
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String[] ips = ip.split(",");
        return ips[0].trim();
    }


    /**
     * 防刷控制
     * @param request
     * @param hosts
     */
    public static void refererCheck(HttpServletRequest request,String ...hosts){
        if(hosts == null){
            return;
        }
        String referer = request.getHeader(HttpHeaders.REFERER);
        if(StringUtils.isBlank(referer)){
            throw new IllegalStateException("abnormal request...");
        }
        for (String host : hosts) {
            if(host == null){
                continue;
            }
            if(referer.contains(host)){
                return;
            }
        }
        throw new IllegalStateException("abnormal request...");
    }

    /**
     * 获取参数签名
     * @param params
     * @return
     */
    public static String getParamSign(Map<String,Object> params){
        return SignUtil.getPaySign(params,null);
    }

    /**
     * 地址展开
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String expandUrl(Map<String,Object> params) throws UnsupportedEncodingException {
        return expandUrl(params, CharsetConsts.DEFAULT_CHARSET);
    }

    public static String expandUrl(Map<String,Object> params,Charset charset) throws UnsupportedEncodingException {
        if(params == null || params.isEmpty()){
            return "";
        }
        StringBuilder str = new StringBuilder();
        for(Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator(); it.hasNext();){
            Map.Entry<String, Object> entry = it.next();
            if(entry.getValue() != null){
                str.append(entry.getKey()+"="+ URLEncoder.encode(String.valueOf(entry.getValue()), charset.name()));
            }
            if(it.hasNext()){
                str.append("&");
            }
        }
        return str.toString();
    }


    /**
     * 不支持duplicate key
     * @param url
     * @return
     */
    public static Map<String,String> decodeUrl(String url){
        if(StringUtils.isBlank(url)){
            return Maps.newHashMap();
        }
        return Splitter.on("&").withKeyValueSeparator("=").split(url);
    }

}
