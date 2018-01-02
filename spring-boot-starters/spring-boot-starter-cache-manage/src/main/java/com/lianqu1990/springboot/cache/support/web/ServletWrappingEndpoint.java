/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lianqu1990.springboot.cache.support.web;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ServletWrappingController;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

//TODO 可以直接使用controller,参考BasicErrorController
//@RequestMapping("${server.error.path:${error.path:/error}}")
//暂时从cloud复制过来使用，后续可能在boot包中直接使用
@Deprecated
public abstract class ServletWrappingEndpoint implements InitializingBean,
        ApplicationContextAware, ServletContextAware, MvcEndpoint {

    // TODO: move to spring-boot?

    private String path;

    private boolean sensitive;

    private boolean enabled = true;

    private final ServletWrappingController controller = new ServletWrappingController();


    @Override
    public void afterPropertiesSet() throws Exception {
        this.controller.afterPropertiesSet();
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.controller.setServletContext(servletContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.controller.setApplicationContext(applicationContext);
    }

    protected ServletWrappingEndpoint(Class<? extends Servlet> servletClass,
                                      String servletName, String path, boolean sensitive, boolean enabled) {
        this.controller.setServletClass(servletClass);
        this.controller.setServletName(servletName);
        this.path = path;
        this.sensitive = sensitive;
        this.enabled = enabled;
    }

    @RequestMapping("**")
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) Map<String,Object> content)
            throws Exception {
        request.setAttribute("REQUEST_BODY_WRAPPER", content);
        return this.controller.handleRequest(request, response);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public ServletWrappingController getController() {
        return this.controller;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public boolean isSensitive() {
        return this.sensitive;
    }

    @Override
    public Class<? extends Endpoint<?>> getEndpointType() {
        return null;
    }

}
