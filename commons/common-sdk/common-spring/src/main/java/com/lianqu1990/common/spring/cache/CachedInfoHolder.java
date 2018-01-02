package com.lianqu1990.common.spring.cache;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hanchao
 * @date 2017/10/7 10:29
 */
public class CachedInfoHolder {
    private static Map<Integer, Cached> caches = new LinkedHashMap<>();
    private static AtomicInteger idGenerator = new AtomicInteger(0);

    /**
     *
     * @param bean
     * @param cached
     */
    protected static void addCache(Cached cached, Object bean) {
        caches.put(idGenerator.incrementAndGet(), cached);
    }

    /**
     * 获取所有的缓存信息
     *
     * @return
     */
    public static List<CachedInfo> getAll() {
        List<CachedInfo> cachedInfoList = new ArrayList<>();
        for (Integer id : caches.keySet()) {
            cachedInfoList.add(transformAnnotation(id, caches.get(id)));
        }
        return cachedInfoList;
    }

    /**
     * 根据id获取cache
     *
     * @param id
     * @return
     */
    public static CachedInfo get(int id) {
        return transformAnnotation(id, caches.get(id));
    }


    @Data
    @Builder
    public static class CachedInfo {
        private int id;
        private String name;
        private String[] key;
        private int cluster;
        private Cached.DataScourseType sourceType;
        private List<CacheParam> params;
    }

    @Data
    @Builder
    public static class CacheParam {
        private String name;
        private String value;
        private Class type;
    }


    private static CachedInfo transformAnnotation(int id, Cached cached) {
        if (cached == null) {
            return null;
        }
        CachedInfo cachedInfo = CachedInfo.builder()
                .id(id)
                .name(cached.name())
                .key(cached.key())
                .cluster(cached.cluster())
                .sourceType(cached.sourceType())
                .build();
        List<CacheParam> params = new ArrayList<>();
        if (cached.params() != null && cached.params().length > 0) {
            for (Cached.Param param : cached.params()) {
                params.add(CacheParam.builder()
                        .name(param.name())
                        .value(param.value())
                        .type(param.type())
                        .build());
            }
        }
        cachedInfo.setParams(params);
        return cachedInfo;
    }

}
