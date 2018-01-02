package com.lianqu1990.springboot.cache.support.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.lianqu1990.common.CommonResult;
import com.lianqu1990.common.SuccessResponse;
import com.lianqu1990.common.spring.cache.Cached;
import com.lianqu1990.common.spring.cache.CachedInfoHolder;
import com.lianqu1990.common.utils.collection.HashMapBuilder;
import com.lianqu1990.common.utils.reflect.BeanUtil;
import com.lianqu1990.common.utils.web.RequestUtil;
import com.lianqu1990.springboot.cache.spel.SpelExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.lianqu1990.springboot.cache.conf.CacheManageConsts.*;

/**
 * web缓存管理端点
 * @author hanchao
 * @date 2017/10/7 14:39
 */
@Deprecated
public class CacheManageServlet extends HttpServlet {

    public static Map<Cached.DataScourseType,String> prefix = Maps.newHashMap();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String method = Optional.ofNullable(req.getParameter("_action")).orElse("");
        switch (method){
            case METHOD_QUERY:
                query(req,resp);
                break;
            case METHOD_GET:
                get(req,resp);
                break;
            case METHOD_GETINSIDE:
                getInside(req,resp);
                break;
            case METHOD_DELINSIDE:
                delInside(req,resp);
                break;
            default:
                query(req,resp);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String protocol = req.getProtocol();
        if (protocol.endsWith("1.1")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "禁止访问！");
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "禁止访问！");
        }
    }


    /**
     * 展示所有的cache信息
     * @param req
     * @param resp
     * @throws IOException
     */
    private void query(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<CachedInfoHolder.CachedInfo> cachedInfos = CachedInfoHolder.getAll();
        write(resp,new SuccessResponse(cachedInfos));
    }



    public void get(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int cacheId = Optional.ofNullable((Ints.tryParse(req.getParameter("id")))).orElse(0);
        CachedInfoHolder.CachedInfo cachedInfo = CachedInfoHolder.get(cacheId);

        if(cachedInfo == null){
            write(resp,CommonResult.RESOURCE_NOT_FOUND);
        }
        if(cachedInfo.getSourceType() == Cached.DataScourseType.GETINSIDE || cachedInfo.getSourceType() == Cached.DataScourseType.DELINSIDE){
            write(resp, CommonResult.FORBIDDEN);
        }

        Object execute = executeRequest(req, cachedInfo);
        Map result = HashMapBuilder.newBuilder()
                .put("prefix",prefix.containsKey(cachedInfo.getSourceType()) ? prefix.get(cachedInfo.getSourceType()) : "")
                .put("key",execute)
                .build();
        write(resp,new SuccessResponse(result));
    }


    public void getInside(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int cacheId = Optional.ofNullable((Ints.tryParse(req.getParameter("id")))).orElse(0);
        CachedInfoHolder.CachedInfo cachedInfo = CachedInfoHolder.get(cacheId);
        if(cachedInfo == null){
            write(resp,CommonResult.RESOURCE_NOT_FOUND);
        }
        if(cachedInfo.getSourceType() != Cached.DataScourseType.GETINSIDE){
            write(resp,CommonResult.FORBIDDEN);
        }

        Object result = executeRequest(req, cachedInfo);
        write(resp,new SuccessResponse(result));

    }

    public void delInside(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        int cacheId = Optional.ofNullable((Ints.tryParse(req.getParameter("id")))).orElse(0);
        CachedInfoHolder.CachedInfo cachedInfo = CachedInfoHolder.get(cacheId);
        if(cachedInfo == null){
            write(resp,CommonResult.RESOURCE_NOT_FOUND);
        }
        if(cachedInfo.getSourceType() != Cached.DataScourseType.DELINSIDE){
            write(resp,CommonResult.FORBIDDEN);
        }

        Object result = executeRequest(req, cachedInfo);
        write(resp,new SuccessResponse(result));
    }

    /**
     * 输出响应
     * @param resp
     * @param o
     * @throws IOException
     */
    private void write(HttpServletResponse resp,Object o) throws IOException {
        PrintWriter writer = resp.getWriter();
        writer.print(JSON.toJSONString(o));
    }

    /**
     * 获取解析器
     * @param req
     * @return
     */
    private SpelExecutor getExecutor(HttpServletRequest req){
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(req.getServletContext());
        return context.getBean(SpelExecutor.class);
    }

    /**
     * 执行请求
     * @param req
     * @param cachedInfo
     * @return
     * @throws IOException
     */
    private Object executeRequest(HttpServletRequest req, CachedInfoHolder.CachedInfo cachedInfo) throws IOException {
        SpelExecutor executor = getExecutor(req);
        Map<String,Object> reqParams = (Map<String, Object>) req.getAttribute("REQUEST_BODY_WRAPPER");
        Map<String,Object> execParams = Maps.newHashMap();
        for (CachedInfoHolder.CacheParam cacheParam : cachedInfo.getParams()) {
            String paramName = cacheParam.getValue();
            Object value = reqParams.get(paramName);
            Class type = cacheParam.getType();
            try {
                execParams.put(paramName,JSON.parseObject(JSON.toJSONString(value),type));
            } catch(JSONException e){
                //可能不是对应的类型,尝试通过url方式转换
                if (value instanceof String){
                    try {
                        Map guessParam = RequestUtil.decodeUrl(String.valueOf(value));
                        if(Map.class.isAssignableFrom(type)){
                            execParams.put(paramName,guessParam);
                        }else{
                            execParams.put(paramName, BeanUtil.fromMap(type,guessParam));
                        }
                    } catch(Exception e1){
                        e.printStackTrace();
                    }
                }else{
                    e.printStackTrace();
                }
            }
        }
        if(cachedInfo.getKey().length > 1 && cachedInfo.getSourceType() == Cached.DataScourseType.REDIS){
            List<Object> result = Lists.newArrayList();
            for (String key : cachedInfo.getKey()) {
                result.add(executor.execute(key, execParams));
            }
            return result;
        }
        return executor.execute(cachedInfo.getKey()[cachedInfo.getKey().length-1], execParams);
    }
}
