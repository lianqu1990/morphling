package com.lianqu1990.common.exception;


import com.lianqu1990.common.CommonResult;

/**
 * @author hanchao
 * @date 2017/8/24 10:01
 */
public class ArgsValidException extends BizException {
    public ArgsValidException(String customMessage) {
        super(CommonResult.INVALID_ARGUMENTS,customMessage);
    }
}
