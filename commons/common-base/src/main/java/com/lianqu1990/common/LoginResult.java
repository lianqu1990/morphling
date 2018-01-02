package com.lianqu1990.common;

/**
 * @author hanchao
 * @date 2017/12/27 17:43
 */
public class LoginResult {

    public static final ErrorResult UNAUTHORIZED = ErrorResult.create(401,"权限不足");
    public static final ErrorResult FORBIDDEN = ErrorResult.create(403,"非法请求");


    public static final ErrorResult LOGIN_FAILED = ErrorResult.create(1100,"登陆失败");
    public static final ErrorResult WRONG_CAPTCHA = ErrorResult.create(1101,"验证码有误");
    public static final ErrorResult USERNAME_NOT_EXIST = ErrorResult.create(1102,"用户名不存在");
    public static final ErrorResult WRONG_PASSWORD = ErrorResult.create(1103,"密码错误");

}
