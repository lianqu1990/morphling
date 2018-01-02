package com.lianqu1990.common;

/**
 * 待用
 * @author hanchao
 * @date 2017/12/20 11:01
 */
public class BaseResult implements Result {
    private int code;
    private String message;
    private Object data;

    public static final BaseResult create(int code){
        return new BaseResult(code,null,null);
    }
    public static final BaseResult create(int code,String message){
        return new BaseResult(code,message,null);
    }

    public static final BaseResult create(int code, String message, Object data){
        return new BaseResult(code,message,data);
    }

    protected BaseResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public int getCode() {
        return code;
    }

    public BaseResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public BaseResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public BaseResult setData(Object data) {
        this.data = data;
        return this;
    }
}
