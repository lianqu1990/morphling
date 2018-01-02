package com.lianqu1990.common.utils.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    /**
     * 删除cookie
     * @param response
     * @param name
     */
    public static void removeCookie(HttpServletResponse response,String name){
        setCookie(response,name,null,null,0,false);
    }
    /**
     * 设置cookie.默认session级别
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        setCookie(response,name,value,null,-1,false);
    }

    /**
     * https://code.google.com/p/util-java/source/browse/trunk/src/utils/
     * <p>
     * CookieUtils.java?r=6
     *
     * @param response
     * @param name
     * @param value
     * @param domain
     * @param maxAge
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String domain, int maxAge,boolean httpOnly) {
        if (value == null) {
            value = "";
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        if (domain != null && !"".equals(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setPath("/");
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (cookieName == null || request == null) {
            return null;
        }
        Cookie[] cks = request.getCookies();
        if (cks == null) {
            return null;
        }
        for (Cookie cookie : cks) {
            if (cookieName.equals(cookie.getName()))
                return cookie.getValue();
        }
        return null;
    }

    /**
     * @param request
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        if (cookieName == null || request == null) {
            return null;
        }
        Cookie[] cks = request.getCookies();
        if (cks == null) {
            return null;
        }
        for (Cookie cookie : cks) {
            if (cookieName.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}