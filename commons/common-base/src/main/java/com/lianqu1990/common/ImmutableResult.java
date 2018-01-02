package com.lianqu1990.common;

/**
 * 待用
 * @author hanchao
 * @date 2017/12/20 11:02
 */
public class ImmutableResult extends BaseResult {
    protected ImmutableResult(int code, String message, Object data) {
        super(code, message, data);
    }

    @Override
    public BaseResult setMessage(String message) {
        return BaseResult.create(this.getCode(),message,this.getData());
    }

    @Override
    public BaseResult setData(Object data) {
        return BaseResult.create(this.getCode(),this.getMessage(),data);
    }
}
