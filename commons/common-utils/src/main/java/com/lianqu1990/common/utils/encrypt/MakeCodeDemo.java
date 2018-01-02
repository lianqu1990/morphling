package com.lianqu1990.common.utils.encrypt;

import com.google.common.collect.Lists;
import com.lianqu1990.common.utils.encode.Base32Util;
import com.lianqu1990.common.utils.encode.Bits40Encode;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 *
 * https://www.zhihu.com/question/29865340/answer/45894223 兑换码生成算法实践
 *
 *
 * 感觉所谓的base24更合适当作进制来用,只是最终都是规律的结果，通过rc4加密以后活的不规律的结果，能保证一个数字，只对应到一个最终的结果，可以反推
 * 编码方式对id换兑换码
 * @author hanchao
 * @date 2017/4/11 12:10
 */
public class MakeCodeDemo {
    private static final long MAX_NUM = 1099511627775L;
    /**
     * 一个byte 8，32编码5，为了不补，直接统一40位
     * 40位编码以后应该是8位  ,最大1099511627776
     * 没有实践40位，简单用int，用byte[4]来表示，base32以后是7位
     * @param args
     */
    public static void main(String[] args){
        for(String a : create(1000)){
            System.out.println(">>>>>  "+ a);
        }
        
        //这里把头部写死了，可以继续把头部的问题单独摘出来，编码处理
    }
    
    public static List<String> create(int size){
        List<String> result = Lists.newArrayList();
        
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long offset = MAX_NUM/size;
        long current = 0;
        for(int i = 0; i <size;i++){
            current = current += random.nextLong(offset);
            result.add(createDetail(current));
        }
        return result;
    }
    
    public static String createDetail(long v){
      //这样是11位，可以再加一位校验位，
        /**
         * 预设活动位,占4为，可以支持65535次活动
         */
        byte[] head = new byte[]{0,1};

        int a = 3;
        String key = "cet";
        byte[] bt = Bits40Encode.bytesEncode(v);
        for (byte b : bt) {
            System.out.println(">>"+b);
        }
        byte[] enc = Rc4Encrypt.encry_RC4_byte(bt,key);//依然是4个字节
        for (byte b : enc) {
            System.out.println(">>"+b);
        }
        String body = Base32Util.encode(enc);
        //7个可以表示到5字节
        //这7个来加密原来的head
        byte[] headEnc = Rc4Encrypt.encry_RC4_byte(head,body);
        String header = Base32Util.encode(headEnc);
        System.out.println("头:"+header);
        String code = header+body;
        System.out.println("体:"+body);
        System.out.println("加密串:"+code);

        //校验位比较简单，有个随意的约定就行，为了前端校验速度而已，也可以不加
        byte tmp = (byte) (code.hashCode()%32);
        String check = Base32Util.encode(new byte[]{tmp});
        System.out.println("验证段:"+check);

        System.out.println("结果:"+code+check);
        return code+check;
    }

}
