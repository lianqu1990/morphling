package com.huatu.morphling.common.consts;

/**
 * @author hanchao
 * @date 2017/12/19 17:57
 */
public class CacheManageConsts {
    public static final String CACHE_MANAGE_URL = "http://%s:%d/%s"+ EndpointConsts.DEFAULT_CONTEXT+"/cacheManage?_action=%s&id=%s";

    /**
     * 查看所有的缓存信息
     */
    public static final String METHOD_QUERY = "query";
    /**
     * 从目标机器获取key
     */
    public static final String METHOD_GET = "get";
    /**
     * 从目标机器获取具体的缓存信息,local cache
     */
    public static final String METHOD_GETINSIDE = "getInside";
    /**
     * 从目标机器删除缓存,local cache
     */
    public static final String METHOD_DELINSIDE = "delInside";
}
