package com.huatu.morphling.web.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.huatu.common.ErrorResult;
import com.huatu.common.SuccessResponse;
import com.huatu.common.exception.BizException;
import com.huatu.morphling.common.bean.AppInstanceVO;
import com.huatu.morphling.common.consts.CacheManageConsts;
import com.huatu.morphling.dao.jpa.entity.Env;
import com.huatu.morphling.service.local.AppService;
import com.huatu.morphling.service.local.EnvService;
import com.huatu.morphling.utils.collection.HashMapBuilder;
import okhttp3.*;
import okhttp3.RequestBody;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * @author hanchao
 * @date 2017/12/19 17:26
 */
@RequestMapping("/cache")
@RestController
public class CacheManageController {
    @Autowired
    private AppService appService;
    @Autowired
    private EnvService envService;

    @Autowired
    protected OkHttpClient okHttpClient;

    private static Map<String,JedisCommands> CONNECTION_CACHE = new ConcurrentHashMap();


    @GetMapping("/dataSources")
    public Object listDataSources(@CookieValue String env){
        Env.EnvProperties properties = envService.getEnvs().get(env).getProperties();
        Map<String,Object> dataSources = HashMapBuilder.newBuilder()
                .put("redis",properties.getRedises())
                .buildUnsafe();
        return dataSources;
    }


    @GetMapping("/preview/{appId}")
    public Object preview(@PathVariable int appId){
        List<AppInstanceVO> instances = appService.findInstances(appId);
        if(CollectionUtils.isEmpty(instances)){
            return Lists.newArrayList();
        }else{
            for (AppInstanceVO instance : instances) {
                String url = String.format(CacheManageConsts.CACHE_MANAGE_URL,instance.getHost(),instance.getPort(),instance.getContextPath(), CacheManageConsts.METHOD_QUERY,"");
                RequestBody requestBody = RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE), "");
                Response response = null;
                try {
                    response = okHttpClient.newCall(new Request.Builder().url(url.replace("//","/")).post(requestBody).build()).execute();
                    String responseStr = response.body().string();
                    return JSON.parseObject(responseStr, SuccessResponse.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return Lists.newArrayList();
        }
    }

    @PostMapping(value = "/{appId}/{cacheId}",params = "type=REDIS")
    public Object getRedisCache(@PathVariable int appId,
                                @PathVariable int cacheId,
                                @RequestParam(required = false,defaultValue = "0") int clusterId,
                                @CookieValue String env,
                                @org.springframework.web.bind.annotation.RequestBody  Map params) throws UnsupportedEncodingException {
        params.remove("type");
        List<AppInstanceVO> instances = appService.findInstances(appId);
        for (AppInstanceVO instance : instances) {
            String url = String.format(CacheManageConsts.CACHE_MANAGE_URL,instance.getHost(),instance.getPort(),instance.getContextPath(), CacheManageConsts.METHOD_GET,cacheId);
            RequestBody requestBody = RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE), JSON.toJSONString(params));
            Response response = null;
            try {
                response = okHttpClient.newCall(new Request.Builder().url(url.replace("//","/")).post(requestBody).build()).execute();
                String responseStr = response.body().string();
                SuccessResponse data = JSON.parseObject(responseStr, SuccessResponse.class);
                Map<String,Object> cacheKeys = (Map<String, Object>) data.getData();
                //业务prefix
                String prefix = String.valueOf(cacheKeys.get("prefix"));
                if (!prefix.endsWith(".")) {
                    prefix = prefix + ".";
                }

                Env.RedisDataSource redisDataSource = envService.getRedisDataSource(env, clusterId);

                if(redisDataSource == null){
                    return null;
                }

                JedisCommands commands = getResource(redisDataSource);

                if(cacheKeys.get("key") instanceof List){
                    List<String> keys = (List<String>) cacheKeys.get("key");
                    //map缓存
                    return commands.hget(prefix+keys.get(0),keys.get(1));
                }else{
                    return commands.get(prefix+String.valueOf(cacheKeys.get("key")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @PostMapping(value = "/del/{appId}/{cacheId}",params = "type=REDIS")
    public Object delRedisCache(@PathVariable int appId,
                                @PathVariable int cacheId,
                                @RequestParam(required = false,defaultValue = "0") int clusterId,
                                @CookieValue String env,
                                @org.springframework.web.bind.annotation.RequestBody  Map params) throws UnsupportedEncodingException {
        params.remove("type");
        List<AppInstanceVO> instances = appService.findInstances(appId);
        for (AppInstanceVO instance : instances) {
            String url = String.format(CacheManageConsts.CACHE_MANAGE_URL,instance.getHost(),instance.getPort(),instance.getContextPath(), CacheManageConsts.METHOD_GET,cacheId);
            RequestBody requestBody = RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE), JSON.toJSONString(params));
            Response response = null;
            try {
                response = okHttpClient.newCall(new Request.Builder().url(url.replace("//","/")).post(requestBody).build()).execute();
                String responseStr = response.body().string();
                SuccessResponse data = JSON.parseObject(responseStr, SuccessResponse.class);
                Map<String,Object> cacheKeys = (Map<String, Object>) data.getData();
                //业务prefix
                String prefix = String.valueOf(cacheKeys.get("prefix"));
                if (!prefix.endsWith(".")) {
                    prefix = prefix + ".";
                }

                Env.RedisDataSource redisDataSource = envService.getRedisDataSource(env, clusterId);

                if(redisDataSource == null){
                    return null;
                }

                JedisCommands commands = getResource(redisDataSource);

                if(cacheKeys.get("key") instanceof List){
                    List<String> keys = (List<String>) cacheKeys.get("key");
                    //map缓存
                    return commands.hdel(prefix+keys.get(0),keys.get(1));
                }else{
                    return commands.del(prefix+String.valueOf(cacheKeys.get("key")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    @GetMapping("/redis")
    public Object getRedis(@CookieValue String env, int clusterId,String type,String key,@RequestParam(required = false) String hashkey){
        Env.RedisDataSource redisDataSource = envService.getRedisDataSource(env, clusterId);
        JedisCommands jedisCommands = getResource(redisDataSource);
        switch (type){
            case "string":
                return jedisCommands.get(key);
            case "hash":
                return jedisCommands.hget(key,hashkey);
            default:
                break;
        }
        return null;
    }

    @DeleteMapping("/redis")
    public Object delRedis(@CookieValue String env, int clusterId,String type,String key,@RequestParam(required = false) String hashkey){
        Env.RedisDataSource redisDataSource = envService.getRedisDataSource(env, clusterId);
        JedisCommands jedisCommands = getResource(redisDataSource);
        switch (type){
            case "string":
                return jedisCommands.del(key);
            case "hash":
                return jedisCommands.hdel(key,hashkey);
            default:
                break;
        }
        return null;
    }

    @PostMapping(value = "/{appId}/{cacheId}",params = "type=GETINSIDE")
    public Object getInsideCache(@PathVariable int appId, @PathVariable int cacheId, HttpServletRequest request){
        return null;
    }



    private JedisCommands getResource(Env.RedisDataSource redisDataSource){
        String address = redisDataSource.getAddress();
        if(CONNECTION_CACHE.get(address) == null){
            synchronized (this){
                if(CONNECTION_CACHE.get(address) == null){
                    if(redisDataSource.getType() == Env.RedisType.CLUSTER){
                        Set<HostAndPort> hostAndPorts = Arrays.stream(address.split(",")).map(addr -> {
                            String[] hostAndPort = addr.split(":");
                            return new HostAndPort(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
                        }).collect(Collectors.toSet());
                        JedisCluster jedisCluster = new JedisCluster(hostAndPorts);
                        CONNECTION_CACHE.put(address,jedisCluster);
                        return jedisCluster;
                    }else{
                        throw new BizException(ErrorResult.create(-1,"该REDIS类型暂不支持"));
                    }
                }
            }
        }
        return CONNECTION_CACHE.get(address);
    }
}
