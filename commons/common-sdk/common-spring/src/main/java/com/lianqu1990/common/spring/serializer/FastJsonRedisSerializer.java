package com.lianqu1990.common.spring.serializer;

import com.lianqu1990.common.serialize.fastjson.FastJsonSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author hanchao
 * @date 2017/10/25 10:29
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer {
    private FastJsonSerializer fastJsonSerializer;
    public FastJsonRedisSerializer(){
        this(new FastJsonSerializer());
    }
    public FastJsonRedisSerializer(FastJsonSerializer fastJsonSerializer){
        this.fastJsonSerializer = fastJsonSerializer;
    }
    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return fastJsonSerializer.serialize(o);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return fastJsonSerializer.deserialize(bytes);
    }
}
