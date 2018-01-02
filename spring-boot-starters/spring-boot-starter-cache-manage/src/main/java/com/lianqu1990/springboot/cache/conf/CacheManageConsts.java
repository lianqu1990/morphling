package com.lianqu1990.springboot.cache.conf;

/**
 * @author hanchao
 * @date 2017/10/7 19:44
 */
public class CacheManageConsts {
    //servlet相关
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
