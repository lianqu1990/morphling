package com.huatu.morphling.web.controller;

import com.huatu.morphling.service.local.EnvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hanchao
 * @date 2017/12/4 18:18
 */
@RestController
@RequestMapping("/env")
public class EnvController {
    @Autowired
    private EnvService envService;
    @GetMapping
    public Object list(){
        return envService.getEnvs().values();
    }
}
