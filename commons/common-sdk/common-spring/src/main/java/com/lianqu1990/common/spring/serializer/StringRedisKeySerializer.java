package com.lianqu1990.common.spring.serializer;

import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

/**
 * @author hanchao
 * @date 2017/9/13 20:56
 */
public class StringRedisKeySerializer extends StringRedisSerializer {
    public static final String delimiter = ".";//业务分隔符
    private String prefix;
    private int prefixLength;

    public StringRedisKeySerializer(){
        super();
    }
    public StringRedisKeySerializer(String prefix){
        super();
        Assert.notNull(prefix,"key prefix cant be null!");
        if(prefix.endsWith(delimiter)){
            this.prefix = prefix;
        }else{
            this.prefix = prefix+delimiter;
        }
        this.prefixLength = prefix.length();
    }
    public StringRedisKeySerializer(String prefix, Charset charset){
        super(charset);
        this.prefix = prefix;
    }

    public String deserialize(byte[] bytes) {
        String result = super.deserialize(bytes);
        return result != null && result.startsWith(prefix) ? result : result.substring(prefixLength);
    }

    public byte[] serialize(String string) {
        return string == null ? null : super.serialize(prefix+string);
    }

    public String getPrefix() {
        return prefix;
    }
}
