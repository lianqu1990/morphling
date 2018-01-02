package com.lianqu1990.common.utils.encode;

import com.google.common.base.Preconditions;

/**
 * @author hanchao
 * @date 2017年4月12日 下午3:48:29
 */
public class Bits40Encode {
    /**
     * 40bits数字转bytes
     * @param value
     * @return
     */
    public static byte[] bytesEncode(long value){
        Preconditions.checkArgument(value >> 40 == 0 && value > 0,"value only can between 0 and 1099511627775...");
        byte[] src = new byte[5];
        src[4] =  (byte) ((value>>32) & 0xFF);
        src[3] =  (byte) ((value>>24) & 0xFF);
        src[2] =  (byte) ((value>>16) & 0xFF);
        src[1] =  (byte) ((value>>8) & 0xFF);
        src[0] =  (byte) (value & 0xFF);
        return src;
    }
    
    public static long bytesDecode(byte[] src) {
        Preconditions.checkArgument(src.length == 5);
        long value;
        value = (long) ((src[0] & 0xFF)
                | ((src[1] & 0xFF)<<8)
                | ((src[2] & 0xFF)<<16)
                | ((src[3] & 0xFFL)<<24)
                | ((src[4] & 0xFFL)<<32));
        return value;
    }
}
