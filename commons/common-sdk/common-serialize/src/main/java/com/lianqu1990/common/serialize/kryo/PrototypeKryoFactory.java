package com.lianqu1990.common.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author hanchao
 * @date 2017/9/6 18:25
 */
public class PrototypeKryoFactory extends KryoFactory {
    public Kryo getKryo() {
        return createKryo();
    }
    @Override
    protected Kryo createKryo() {
        return new Kryo();
    }
}
