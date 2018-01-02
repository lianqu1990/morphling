package com.lianqu1990.common.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hanchao
 * @date 2017/9/6 18:08
 */
@Slf4j
public class CompatibleKryo extends Kryo {

    @Override
    public Serializer getDefaultSerializer(Class type) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }

        if (!type.isArray() && !checkZeroArgConstructor(type)) {
            if (log.isWarnEnabled()) {
                log.warn(type + " has no zero-arg constructor and this will affect the serialization performance");
            }
            return new JavaSerializer();
        }
        return super.getDefaultSerializer(type);
    }

    protected static boolean checkZeroArgConstructor(Class clazz) {
        try {
            clazz.getDeclaredConstructor();
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}

