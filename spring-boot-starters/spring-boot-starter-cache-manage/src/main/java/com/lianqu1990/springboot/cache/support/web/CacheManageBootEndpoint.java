package com.lianqu1990.springboot.cache.support.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lianqu1990.common.CommonResult;
import com.lianqu1990.common.Result;
import com.lianqu1990.common.SuccessResponse;
import com.lianqu1990.common.spring.cache.Cached;
import com.lianqu1990.common.spring.cache.CachedInfoHolder;
import com.lianqu1990.common.spring.web.MediaType;
import com.lianqu1990.common.utils.collection.HashMapBuilder;
import com.lianqu1990.common.utils.reflect.BeanUtil;
import com.lianqu1990.common.utils.web.RequestUtil;
import com.lianqu1990.springboot.cache.spel.SpelExecutor;
import com.lianqu1990.springboot.cache.support.CacheManageEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.lianqu1990.springboot.cache.conf.CacheManageConsts.*;

/**
 * @author hanchao
 * @date 2017/10/7 16:35
 */
@CacheManageEndPoint
public class CacheManageBootEndpoint  implements MvcEndpoint {

    public static Map<Cached.DataScourseType,String> prefix = Maps.newHashMap();

    @Autowired
    private SpelExecutor spelExecutor;


    @Override
    public String getPath() {
        return "/cacheManage";
    }

    @Override
    public boolean isSensitive() {
        return true;
    }

    @Override
    public Class<? extends Endpoint> getEndpointType() {
        return null;
    }

    @PostMapping(consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public Object handle(@RequestParam("_action")String action,
                         @RequestParam(value = "id",required = false,defaultValue = "0")int cacheId,
                         @RequestBody(required = false)Map<String,Object> params) throws IOException {
        Object result;
        switch (action){
            case METHOD_QUERY:
                result = queryAll();
                break;
            case METHOD_GET:
                result = getKey(cacheId,params);
                break;
            case METHOD_GETINSIDE:
            case METHOD_DELINSIDE:
                result = dealInside(cacheId,params);
                break;
            default:
                result = queryAll();
                break;
        }
        if(result instanceof Result){
            return result;
        }else{
            return new SuccessResponse(result);
        }
    }



    /**
     * 展示所有的cache信息
     * @throws IOException
     */
    private Object queryAll() throws IOException {
        List<CachedInfoHolder.CachedInfo> cachedInfos = CachedInfoHolder.getAll();
        return cachedInfos;
    }



    public Object getKey(int cacheId, Map<String,Object> params) throws IOException {
        CachedInfoHolder.CachedInfo cachedInfo = CachedInfoHolder.get(cacheId);

        if(cachedInfo == null){
            return CommonResult.RESOURCE_NOT_FOUND;
        }
        if(cachedInfo.getSourceType() == Cached.DataScourseType.GETINSIDE || cachedInfo.getSourceType() == Cached.DataScourseType.DELINSIDE){
            return CommonResult.FORBIDDEN;
        }

        Object execute = executeRequest(params, cachedInfo);
        Map result = HashMapBuilder.newBuilder()
                .put("prefix",prefix.containsKey(cachedInfo.getSourceType()) ? prefix.get(cachedInfo.getSourceType()) : "")
                .put("key",execute)
                .build();
        return result;
    }


    public Object dealInside(int cacheId, Map<String,Object> params) throws IOException {
        CachedInfoHolder.CachedInfo cachedInfo = CachedInfoHolder.get(cacheId);
        if(cachedInfo == null){
            return CommonResult.RESOURCE_NOT_FOUND;
        }
        if(cachedInfo.getSourceType() != Cached.DataScourseType.GETINSIDE){
            return CommonResult.FORBIDDEN;
        }

        Object result = executeRequest(params, cachedInfo);
        return result;
    }



    /**
     * 执行请求
     * @param params
     * @param cachedInfo
     * @return
     * @throws IOException
     */
    private Object executeRequest(Map<String,Object> params, CachedInfoHolder.CachedInfo cachedInfo) throws IOException {
        Map<String,Object> execParams = Maps.newHashMap();
        for (CachedInfoHolder.CacheParam cacheParam : cachedInfo.getParams()) {
            String paramName = cacheParam.getValue();
            Object value = params.get(paramName);
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
                result.add(spelExecutor.execute(key, execParams));
            }
            return result;
        }
        return spelExecutor.execute(cachedInfo.getKey()[cachedInfo.getKey().length-1], execParams);
    }
}
