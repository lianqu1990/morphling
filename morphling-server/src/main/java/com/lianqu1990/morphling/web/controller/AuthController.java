package com.lianqu1990.morphling.web.controller;

import com.lianqu1990.common.ErrorResult;
import com.lianqu1990.common.Result;
import com.lianqu1990.common.SuccessMessage;
import com.lianqu1990.morphling.bean.UserInfo;
import com.lianqu1990.morphling.common.consts.WebParamConsts;
import com.lianqu1990.morphling.consts.MorphlingResponse;
import com.lianqu1990.morphling.dao.jpa.entity.User;
import com.lianqu1990.morphling.service.local.UserService;
import com.lianqu1990.morphling.spring.resolver.UserSession;
import com.lianqu1990.morphling.spring.security.CaptchaInvalidException;
import com.lianqu1990.common.utils.web.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hanchao
 * @date 2017/10/22 14:58
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Qualifier("coreThreadPool")
    private TaskExecutor taskExecutor;
    /**
     * 适配session过期等的检查
     * @return
     */
    @RequestMapping("/tologin")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result tologin() {
        return MorphlingResponse.UNAUTHORIZED;
    }

    @RequestMapping("/denied")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result denied(){
        return MorphlingResponse.FORBIDDEN;
    }

    /**
     * 获取账户信息的接口
     * @param userInfo
     * @return
     */
    @RequestMapping("/get")
    public UserInfo get(@UserSession UserInfo userInfo){
        return userInfo;
    }


    @RequestMapping(value = "/success",params = "login")
    public UserInfo loginSuccess(@UserSession UserInfo userInfo,HttpServletRequest request){
        taskExecutor.execute(() -> {
            userService.loginSuccess(userInfo.getId(), RequestUtil.getIpAddr(request));
        });
        return userInfo;
    }

    @RequestMapping(value = "/success",params = "logout")
    public Result logoutSuccess(){
        return null;
    }

    /**
     * 登陆失败返回信息
     * @param request
     * @return
     */
    @RequestMapping("/fail")
    public Result fail(HttpServletRequest request) {
        AuthenticationException authenticationException = (AuthenticationException) request.getAttribute(WebParamConsts.SPRING_SECURITY_EX);
        if(authenticationException instanceof CaptchaInvalidException){
            return MorphlingResponse.WRONG_CAPTCHA;
        }else if(authenticationException instanceof UsernameNotFoundException){
            return MorphlingResponse.USERNAME_NOT_EXIST;
        }else if(authenticationException instanceof BadCredentialsException){
            return MorphlingResponse.WRONG_PASSWORD;
        }else{
            String message = authenticationException.getMessage();
            return ErrorResult.create(MorphlingResponse.LOGIN_FAILED.getCode(),message);
        }
    }



    @RequestMapping("/modify")
    public Result modify(@UserSession UserInfo userInfo, String oldPass,String newPass){
        User logined = userService.get(userInfo.getId());
        if(passwordEncoder.matches(oldPass,logined.getPassword())){
            logined.setPassword(passwordEncoder.encode(newPass));
            userService.save(logined);
            return SuccessMessage.create();
        }
        return MorphlingResponse.WRONG_PASSWORD;
    }

}
