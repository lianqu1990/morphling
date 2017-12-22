package com.huatu.morphling.web.controller;

import com.google.common.collect.Lists;
import com.huatu.common.CommonResult;
import com.huatu.common.bean.page.Pager;
import com.huatu.common.consts.DataState;
import com.huatu.morphling.bean.UserInfo;
import com.huatu.morphling.common.bean.AppFilter;
import com.huatu.morphling.common.bean.AppInstanceVO;
import com.huatu.morphling.common.consts.SecurityConsts;
import com.huatu.morphling.common.enums.InstanceStatus;
import com.huatu.morphling.dao.jpa.entity.App;
import com.huatu.morphling.dao.jpa.entity.AppInstance;
import com.huatu.morphling.service.local.AppInstanceService;
import com.huatu.morphling.service.local.AppService;
import com.huatu.morphling.service.local.EnvService;
import com.huatu.morphling.service.remote.regist.RegistChecker;
import com.huatu.morphling.spring.resolver.UserSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author hanchao
 * @date 2017/12/4 17:27
 */
@RestController
@RequestMapping("/app")
public class AppController {
    @Autowired
    private AppService appService;
    @Autowired
    private AppInstanceService appInstanceService;
    @Autowired
    private EnvService envService;
    @Autowired(required = false)
    private List<RegistChecker> registCheckerList;
    @GetMapping
    public Object list(@CookieValue String env,
                       AppFilter filter,
                       Pager pager){
        filter.setEnv(env);
        return appService.findByFilter(filter,pager);
    }

    @GetMapping(value="/{appId}/instances")
    public Object listInstances(@PathVariable int appId){
        return appService.findInstances(appId);
    }

    @PostMapping(value="/{appId}/instances")
    public Object addInstance(@PathVariable int appId,int clientId){
        AppInstance appInstance = new AppInstance();
        appInstance.setAppId(appId);
        appInstance.setClientId(clientId);
        appInstance.setStatus(InstanceStatus.INIT.getCode());
        appInstance.setCurrentVersion("");
        appInstance.setCreateTime(new Date());
        appInstance.setState(DataState.EFFECTIVE);
        appInstanceService.save(appInstance);
        return CommonResult.SUCCESS;
    }

    @DeleteMapping(value="/{appId}/instances/{insId}")
    public Object deleteInstance(@PathVariable int appId,@PathVariable int insId){
        appInstanceService.deleteLogic(insId);//TODO 根据状态，是否需要卸载实例
        return CommonResult.SUCCESS;
    }



    @GetMapping(value="/{appId}/clients")
    public Object listClients(@PathVariable int appId,@CookieValue String env){
        return appService.findClients(appId,env);
    }

    @PutMapping
    public Object edit(@UserSession UserInfo userInfo,
                       App app,@RequestParam(required = false) String[] envs){
        if(app.getId() == null){
            Date current = new Date();
            List<App> appList = Lists.newArrayList();
            for (String env : envs) {
                App application = new App();
                BeanUtils.copyProperties(app,application);
                application.setGitUrl(replaceGitUrl(application.getGitUrl()));
                application.setEnv(env);
                application.setAdminUser("");//TODO 后期调整
                application.setCurrentPackVersion("");
                application.setCreateTime(current);
                application.setCreateUserid(userInfo.getId());
                application.setCreateUsername(userInfo.getUsername());
                application.setProperties(new App.AppProperties());
                application.setState(DataState.EFFECTIVE);
                appList.add(application);
            }
            appService.save(appList);
        }else{
            App application = appService.get(app.getId());
            application.setName(app.getName());
            application.setDescription(app.getDescription());
            application.setContextPath(app.getContextPath());
            application.setPort(app.getPort());
            application.setServiceType(app.getServiceType());
            application.setDeployType(app.getDeployType());
            application.setGitUrl(replaceGitUrl(app.getGitUrl()));
            application.setGitBranch(app.getGitBranch());
            appService.save(application);
        }
        return CommonResult.SUCCESS;
    }

    private String replaceGitUrl(String gitUrl){
        return gitUrl.replace("123.103.86.53","192.168.100.21");
    }



    //-----------------------以下的接口冗余,为了方便控制权限------------------------------
    /**
     * 用户的app列表
     * @param env
     * @param userInfo
     * @return
     */
    @GetMapping("/preview")
    public Object userPreview(@CookieValue String env,@UserSession UserInfo userInfo){
        if(userInfo.getRoles().contains(SecurityConsts.ROLE_SUPER_ADMIN)){
            return appService.findByEnv(env);
        }
        return appService.findUserApps(userInfo.getId(),env);
    }


    /**
     * 分离权限
     */
    @GetMapping("/preview/{appId}")
    public Object appInfo(@PathVariable int appId){
        return appService.get(appId);
    }

    /**
     * 实例列表
     * 分离权限
     */
    @GetMapping("/preview/{appId}/instances")
    public Object instances(@PathVariable int appId,
                            @RequestParam(required = false,defaultValue = "false")boolean checkRegist){
        List<AppInstanceVO> instances = appService.findInstances(appId);

        if(checkRegist){
            App app = appService.get(appId);
            RegistChecker checker = null;
            for (RegistChecker registChecker : registCheckerList) {
                if(registChecker.resolve(app.getServiceType())){
                    checker = registChecker;
                    break;
                }
            }

            if(checker != null){
                final RegistChecker checkerRef = checker;
                instances.parallelStream().forEach(instance -> {
                    boolean health = checkerRef.health(app.getName(), instance.getHost(), instance.getPort(),
                            envService.getEnvs().get(app.getEnv()).getProperties());
                    instance.setRegistStatus(health);
                });
            }
        }
        return instances;
    }
}
