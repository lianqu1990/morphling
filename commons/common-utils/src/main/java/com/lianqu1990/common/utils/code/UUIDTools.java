package com.lianqu1990.common.utils.code;

import java.util.UUID;

/**
 * @author hanchao
 * @date 2017/1/18 0:17
 */
public class UUIDTools {
    public static String create(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-","");
    }
}
