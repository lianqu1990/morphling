package com.lianqu1990.common.serialize;

/**
 * @author hanchao
 * @date 2017/9/6 18:36
 */
public class Serializations {
    public static final byte[] EMPTY_ARRAY = new byte[0];
    public static boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }
}
