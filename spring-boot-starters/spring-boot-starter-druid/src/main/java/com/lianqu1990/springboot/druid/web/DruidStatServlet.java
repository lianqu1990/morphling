package com.lianqu1990.springboot.druid.web;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * TODO 通过wrapper到真正的监控页面查看druid的信息
 * @author hanchao
 * @date 2017/1/14 22:30
 */
@WebServlet(urlPatterns = "/_druid/*",
        initParams={
                @WebInitParam(name="allow",value=""),// IP白名单 (没有配置或者为空，则允许所有访问)
                @WebInitParam(name="deny",value="192.168.16.111"),// IP黑名单 (存在共同时，deny优先于allow)
                @WebInitParam(name="loginUsername",value="admin"),// 用户名
                @WebInitParam(name="loginPassword",value="2017@)!&"),// 密码
                @WebInitParam(name="resetEnable",value="false")// 禁用HTML页面上的“Reset All”功能
        })
@Deprecated
public class DruidStatServlet extends StatViewServlet{
}
