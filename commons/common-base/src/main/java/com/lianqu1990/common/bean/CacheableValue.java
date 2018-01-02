package com.lianqu1990.common.bean;

/**
 * @author hanchao
 * @date 2017/9/29 15:15
 */
public class CacheableValue extends BaseValue {
    private boolean _cache;
    private long _cacheTimestamp;

    public boolean isCache() {
        return _cache;
    }

    public void setCache(boolean cache) {
        this._cache = cache;
    }

    public long getCacheTimestamp() {
        return _cacheTimestamp;
    }

    public void setCacheTimestamp(long cacheTimestamp) {
        this._cacheTimestamp = cacheTimestamp;
    }
}
