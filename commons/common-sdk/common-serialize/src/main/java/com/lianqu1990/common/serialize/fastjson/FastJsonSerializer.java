package com.lianqu1990.common.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lianqu1990.common.serialize.Serializations;
import com.lianqu1990.common.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

/**
 * 需要定义安全类，redis中替代者为genericjackson2json
 * @author hanchao
 * @date 2017/10/15 0:32
 */
@Slf4j
public class FastJsonSerializer implements Serializer {
    private boolean throwOnDeserializeError; // 默认false,不抛出序列化失败的异常

    public FastJsonSerializer(){
        this(false);
    }

    public FastJsonSerializer(boolean throwOnDeserializeError){
        this.throwOnDeserializeError = throwOnDeserializeError;
    }

    @Override
    public byte[] serialize(Object o) {
        if(o == null){
            return Serializations.EMPTY_ARRAY;
        }
        return JSON.toJSONBytes(o, SerializerFeature.WriteClassName);
    }

    @Override
    public Object deserialize(byte[] bytes) {
        if(Serializations.isEmpty(bytes)){
            return null;
        }
        try {
            return JSON.parse(bytes);
        } catch(Exception e){
            if(throwOnDeserializeError){
                throw e;
            }
            log.warn("fastjson deserialize error,now config is set to catch exception silently...",e);
            return null;//反序列化失败，返回null，便于重设缓存等
        }
    }
}
