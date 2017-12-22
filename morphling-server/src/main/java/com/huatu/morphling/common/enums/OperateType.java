package com.huatu.morphling.common.enums;

/**
 * @author hanchao
 * @date 2017/11/10 18:00
 */
public enum OperateType {
    CLIENT_INSTALL((byte)1,"客户端安装"),
    CLIENT_UPDATE((byte)2,"客户端安装"),
    CLIENT_UNINSTALL((byte)3,"客户端卸载"),
    APP_PACKAGE((byte)4,"应用打包"),
    APP_DEPLOY((byte)5,"应用部署"),
    APP_CONTROL((byte)6,"应用管理"),
    DEGRADE((byte)7,"业务降级");
    private final byte code;
    private final String description;
    OperateType(byte code,String description){
        this.code = code;
        this.description = description;
    }

    public static OperateType parse(byte code){
        for (OperateType operateType : OperateType.values()) {
            if(operateType.code == code) {
                return operateType;
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
