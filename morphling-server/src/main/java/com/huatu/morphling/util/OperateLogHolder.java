package com.huatu.morphling.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 使用这个可以轮询来获取执行结果
 * @author hanchao
 * @date 2017/11/20 14:48
 */
public class OperateLogHolder {
    private static final Cache<String,StringBuffer> logCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();
    public static void append(String logId,String log){
        try {
            StringBuffer storage = logCache.get(logId, new Callable<StringBuffer>() {
                @Override
                public StringBuffer call() throws Exception {
                    return new StringBuffer();
                }
            });
            storage.append(log);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static String get(String logId){
        StringBuffer storage = logCache.getIfPresent(logId);
        return storage == null ? null : storage.toString();
    }
}
