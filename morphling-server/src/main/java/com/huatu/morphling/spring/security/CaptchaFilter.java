package com.huatu.morphling.spring.security;


import com.huatu.morphling.common.consts.WebParamConsts;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @auther hanchao
 * @date 2016/12/8 22:02
 */
public class CaptchaFilter implements Filter {
    private RequestMatcher validUrl;

    public CaptchaFilter(String validUrl) {
        this.validUrl = new AntPathRequestMatcher(validUrl);
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(validUrl.matches((HttpServletRequest) request)){
            String code = request.getParameter("captcha");
            Object verify = ((HttpServletRequest)request).getSession().getAttribute(WebParamConsts.CAPTCHA_KEY);
            if (verify == null || ! String.valueOf(verify).equalsIgnoreCase(code)) {
                AuthExceptionHolder.set(new CaptchaInvalidException("验证码错误"));
            }else{
                ((HttpServletRequest)request).getSession().removeAttribute(WebParamConsts.CAPTCHA_KEY);//清除已经验证的验证码
            }
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
