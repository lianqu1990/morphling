package com.lianqu1990.common.serialize;


/**
 * @author hanchao
 * @date 2017/10/12 14:04
 */
public interface Serializer {
    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes);
}
