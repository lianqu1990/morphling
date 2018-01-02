package com.lianqu1990.springboot.druid.web;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * @author hanchao
 * @date 2017/1/14 22:33
 */
@WebFilter(filterName="druidWebStatFilter",urlPatterns="/*",
        initParams={
                @WebInitParam(name="exclusions",value="*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/_druid/*")// 忽略资源
        })
public class DruidStatFilter extends WebStatFilter{
}
