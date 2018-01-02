package com.lianqu1990.common.utils.encrypt;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author hanchao
 * @date 2017/4/9 22:33
 */
public class EncryptUtil {
    public static String sha1(String str){
        return DigestUtils.sha1Hex(str);
    }

    public static String md5(String str){
        return DigestUtils.md5Hex(str);
    }

}
