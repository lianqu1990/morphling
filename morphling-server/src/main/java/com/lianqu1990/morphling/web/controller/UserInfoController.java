package com.lianqu1990.morphling.web.controller;

import com.lianqu1990.common.CommonResult;
import com.lianqu1990.morphling.bean.UserInfo;
import com.lianqu1990.morphling.common.bean.UserinfoFormDTO;
import com.lianqu1990.morphling.dao.jpa.entity.User;
import com.lianqu1990.morphling.service.local.RoleService;
import com.lianqu1990.morphling.service.local.UserService;
import com.lianqu1990.morphling.spring.resolver.UserSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author hanchao
 * @date 2017/11/8 10:21
 */
@RequestMapping("/userinfo")
@RestController
public class UserInfoController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping
    public Object getUserInfo(@UserSession UserInfo userInfo){
        User logined = userService.get(userInfo.getId());
        logined.setPassword("");
        return logined;
    }
    @PostMapping
    public Object setUserInfo(@UserSession UserInfo userInfo, @Valid UserinfoFormDTO userinfoFormDTO){
        User logined = userService.get(userInfo.getId());
        BeanUtils.copyProperties(userinfoFormDTO,logined);
        userService.save(logined);
        return CommonResult.SUCCESS;
    }

}
