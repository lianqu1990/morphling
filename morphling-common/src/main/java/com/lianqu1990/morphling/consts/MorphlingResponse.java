package com.lianqu1990.morphling.consts;


import com.lianqu1990.common.ErrorResult;

/**
 * @author hanchao
 * @date 2017/11/3 18:04
 */
public class MorphlingResponse {

    public static final ErrorResult UNAUTHORIZED = ErrorResult.create(401,"权限不足");
    public static final ErrorResult FORBIDDEN = ErrorResult.create(403,"非法请求");


    public static final ErrorResult LOGIN_FAILED = ErrorResult.create(1100,"登陆失败");
    public static final ErrorResult WRONG_CAPTCHA = ErrorResult.create(1101,"验证码有误");
    public static final ErrorResult USERNAME_NOT_EXIST = ErrorResult.create(1102,"用户名不存在");
    public static final ErrorResult WRONG_PASSWORD = ErrorResult.create(1103,"密码错误");

    public static final ErrorResult PACKAGE_BRANCH = ErrorResult.create(1201,"请先选择分支");
    public static final ErrorResult NO_PACKAGE = ErrorResult.create(1202,"请先打包");


    public static final ErrorResult CONNECTION_ERROR = ErrorResult.create(1501,"连接异常");

}
