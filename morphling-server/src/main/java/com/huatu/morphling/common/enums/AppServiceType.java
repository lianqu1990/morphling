package com.huatu.morphling.common.enums;

/**
 * 用来区分是否要注册到nginx
 * @author hanchao
 * @date 2017/12/4 17:50
 */
public enum AppServiceType {
    GATEWAY((byte)1,"网关"),
    SERVICE((byte)2,"服务");
    private final byte code;
    private final String description;
    AppServiceType(byte code,String description){
        this.code = code;
        this.description = description;
    }

    public static AppServiceType parse(byte code){
        for (AppServiceType type : AppServiceType.values()) {
            if(type.code == code) {
                return type;
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
