package com.lianqu1990.common.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author hanchao
 * @date 2017/9/6 18:26
 */
public class ThreadLocalKryoFactory extends KryoFactory {

    private final ThreadLocal<Kryo> holder  = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            return createKryo();
        }
    };

    public Kryo getKryo() {
        return holder.get();
    }
}
