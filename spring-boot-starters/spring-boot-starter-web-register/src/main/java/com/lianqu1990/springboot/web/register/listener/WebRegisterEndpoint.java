package com.lianqu1990.springboot.web.register.listener;

import com.lianqu1990.springboot.web.register.WebRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hanchao
 * @date 2017/12/6 17:33
 */
//@RestController
//@RequestMapping("/admin/webRegister")
public class WebRegisterEndpoint implements MvcEndpoint {
    @Autowired
    private WebRegister webRegister;
    @PostMapping
    @ResponseBody
    public Object control(@RequestParam("_action")String action){
        Object result = false;
        //不允许注销，因为注销后将无法直接恢复
        switch (action){
            case "regist":
                result = webRegister.regist();
                break;
            case "pause":
                result = webRegister.pause();
                break;
            case "resume":
                result = webRegister.resume();
                break;
            default:
                break;
        }
        return result;
    }

    @GetMapping
    @ResponseBody
    public Object state(){
        return webRegister.state();
    }

    @Override
    public String getPath() {
        return "/webRegister";
    }

    @Override
    public boolean isSensitive() {
        return false;
    }

    @Override
    public Class<? extends Endpoint> getEndpointType() {
        return null;
    }
}
