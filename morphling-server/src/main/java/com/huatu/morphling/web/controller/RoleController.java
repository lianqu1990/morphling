package com.huatu.morphling.web.controller;

import com.huatu.morphling.common.consts.SecurityConsts;
import com.huatu.morphling.service.local.RoleService;
import com.huatu.morphling.service.local.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * @author hanchao
 * @date 2017/12/4 10:43
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @GetMapping
    public Object list(){
        return roleService.findAll().stream().filter(x -> !x.getRole().equals(SecurityConsts.ROLE_SUPER_ADMIN)).collect(Collectors.toList());
    }

}
