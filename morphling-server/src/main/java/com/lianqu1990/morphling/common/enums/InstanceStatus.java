package com.lianqu1990.morphling.common.enums;

/**
 * 用来区分是否要注册到nginx
 * @author hanchao
 * @date 2017/12/4 17:50
 */
public enum InstanceStatus {
    INIT((byte)0,"初始化"),
    RUNNING((byte)1,"启动"),
    STOPPED((byte)2,"停止");
    private final byte code;
    private final String description;
    InstanceStatus(byte code, String description){
        this.code = code;
        this.description = description;
    }

    public static InstanceStatus parse(byte code){
        for (InstanceStatus type : InstanceStatus.values()) {
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
