package com.huatu.morphling.enums;

/**
 * 统一定义，方便返回
 * 但是枚举不便于扩展
 */
public enum ResponseState {
    //基础状态码
    SUCCESS(0,"数据正常"),
    FAILED(-1,"请求失败"),

    BAD_REQUEST(400,"参数有误"),
    UNAUTHORIZED(401,"权限不足"),
    FORBIDDEN(403,"非法请求"),
    NOT_FOUND(404,"访问的地址不存在"),

    SERVER_ERROR(500,"服务器繁忙");




    private final Integer code;
    private final String message;
    ResponseState(Integer code, String message){
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
