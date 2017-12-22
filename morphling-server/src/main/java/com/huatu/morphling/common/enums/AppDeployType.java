package com.huatu.morphling.common.enums;

/**
 * 用来区分是否要注册到nginx
 * @author hanchao
 * @date 2017/12/4 17:50
 */
public enum AppDeployType {
    NORMAL((byte)1,"默认"),
    DOCKER((byte)2,"docker");
    private final byte code;
    private final String description;
    AppDeployType(byte code, String description){
        this.code = code;
        this.description = description;
    }

    public static AppDeployType parse(byte code){
        for (AppDeployType type : AppDeployType.values()) {
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
