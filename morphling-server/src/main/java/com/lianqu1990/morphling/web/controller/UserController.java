package com.lianqu1990.morphling.web.controller;

import com.lianqu1990.common.CommonResult;
import com.lianqu1990.common.bean.page.Pager;
import com.lianqu1990.common.consts.DataState;
import com.lianqu1990.morphling.common.bean.AppFilter;
import com.lianqu1990.morphling.common.bean.UserFilter;
import com.lianqu1990.morphling.dao.jpa.entity.User;
import com.lianqu1990.morphling.dao.jpa.entity.UserApp;
import com.lianqu1990.morphling.service.local.AppService;
import com.lianqu1990.morphling.service.local.RoleService;
import com.lianqu1990.morphling.service.local.UserAppService;
import com.lianqu1990.morphling.service.local.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author hanchao
 * @date 2017/12/4 13:55
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AppService appService;
    @Autowired
    private UserAppService userAppService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @DeleteMapping("{userId}")
    public Object delete(@PathVariable int userId){
        userService.delete(userId);
        return null;
    }

    @GetMapping
    public Object list(Pager pager, UserFilter userFilter){
        Pager result = userService.findByFilter(userFilter,pager);
        return result;
    }

    @PostMapping
    public Object add(String username,String name){
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("000000"));//默认密码
        user.setPhone("");
        user.setEmail("");
        user.setHeadImg("");
        user.setLastLoginIp("");
        user.setCreateTime(new Date());
        user.setLastLoginTime(new Date());
        user.setState(DataState.EFFECTIVE);
        userService.save(user);
        return null;
    }


    @GetMapping("/{userId}/roles")
    public Object getRoles(@PathVariable int userId){
        return roleService.findByUserId(userId);
    }

    @PostMapping("/{userId}/roles")
    public Object setRole(@RequestParam("roleId") List<Integer> roleIds, @PathVariable int userId){
        userService.setRole(roleIds,userId);
        return null;
    }


    @GetMapping("/{userId}/apps")
    public Object listApps(@PathVariable int userId){
        return appService.findByUser(userId);
    }
    @PostMapping("/{userId}/apps")
    public Object addApps(@PathVariable int userId,@RequestParam("appIds")List<Integer> appIds){
        for (Integer appId : appIds) {
            UserApp userApp = new UserApp();
            userApp.setAppId(appId);
            userApp.setUserId(userId);
            userApp.setCreateTime(new Date());
            userApp.setState(DataState.EFFECTIVE);
            userAppService.save(userApp);
        }
        return CommonResult.SUCCESS;
    }

    @DeleteMapping("/{userId}/apps/{id}")
    public Object deleteApp(@PathVariable int userId,@PathVariable("id")int id){
        userAppService.delete(id);
        return CommonResult.SUCCESS;
    }

    @GetMapping(value = "/{userId}/apps",params = "exclude")
    public Object listAppsExclude(@PathVariable int userId, AppFilter appFilter,Pager pager){
        return appService.findExcludeByFilter(userId,appFilter,pager);
    }
}
