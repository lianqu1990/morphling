package com.lianqu1990.common.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.lianqu1990.common.serialize.Serializations;
import com.lianqu1990.common.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

/**
 * @author hanchao
 * @date 2017/9/6 18:27
 */
@Slf4j
public class KryoSerializer implements Serializer {

    private KryoFactory kryoFactory;
    private boolean throwOnDeserializeError; // 默认false,不抛出序列化失败的异常

    public KryoSerializer(){
        this(new PooledKryoFactory(),false);
    }

    public KryoSerializer(KryoFactory kryoFactory, boolean throwOnDeserializeError) {
        this.kryoFactory = kryoFactory;
        this.throwOnDeserializeError = throwOnDeserializeError;
    }


    public byte[] serialize(Object o){
        if(o == null){
            return Serializations.EMPTY_ARRAY;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        Kryo kryo = null;
        Output output = null;
        try {
            output = new Output(bos);
            kryo = kryoFactory.getKryo();
            kryo.writeClassAndObject(output, o);
            output.flush();
            return bos.toByteArray();
        } catch(Exception e){
            throw e;
        } finally {
            kryoFactory.returnKryo(kryo);
            if(output != null){
                output.close();
            }
        }
    }

    public Object deserialize(byte [] bytes){
        if(Serializations.isEmpty(bytes)){
            return null;
        }
        Input input = new Input(bytes);
        Kryo kryo = null;
        try {
            kryo = kryoFactory.getKryo();
            return kryo.readClassAndObject(input);
        } catch (KryoException e){
            if(throwOnDeserializeError){
                throw e;
            }
            log.warn("kryo deserialize error,now config is set to catch exception silently...",e);
            return null;//反序列化失败，返回null，便于重设缓存等
        }catch(Exception e){
            throw e;
        } finally {
            kryoFactory.returnKryo(kryo);
        }
    }
}
