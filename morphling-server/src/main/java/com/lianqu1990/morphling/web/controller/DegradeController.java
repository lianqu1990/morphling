package com.lianqu1990.morphling.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lianqu1990.common.CommonResult;
import com.lianqu1990.morphling.bean.ExecResult;
import com.lianqu1990.morphling.bean.UserInfo;
import com.lianqu1990.morphling.common.bean.AppInstanceVO;
import com.lianqu1990.morphling.common.bean.degrade.DegradeEndpoint;
import com.lianqu1990.morphling.common.bean.degrade.ItemRequesetDTO;
import com.lianqu1990.morphling.common.bean.degrade.NamespaceConfig;
import com.lianqu1990.morphling.common.bean.degrade.ReleaseApplyDTO;
import com.lianqu1990.morphling.common.consts.DegradeConsts;
import com.lianqu1990.morphling.common.enums.OperateType;
import com.lianqu1990.morphling.dao.jpa.entity.App;
import com.lianqu1990.morphling.dao.jpa.entity.OperateLog;
import com.lianqu1990.morphling.dao.jpa.entity.ShellLog;
import com.lianqu1990.morphling.service.local.AppService;
import com.lianqu1990.morphling.service.local.EnvService;
import com.lianqu1990.morphling.service.local.OperateLogService;
import com.lianqu1990.morphling.service.local.ShellLogService;
import com.lianqu1990.morphling.service.remote.endpoint.DegradeEndpointService;
import com.lianqu1990.morphling.spring.resolver.UserSession;
import com.lianqu1990.common.utils.code.IdCenter;
import com.lianqu1990.common.utils.date.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hanchao
 * @date 2017/12/21 14:46
 */
@RequestMapping("/degrade")
@RestController
public class DegradeController {
    @Autowired
    private AppService appService;
    @Autowired
    private EnvService envService;
    @Autowired
    private DegradeEndpointService degradeEndpointService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private ShellLogService shellLogService;

    /**
     * 获取当前的降级配置
     * @param appId
     * @return
     */
    @GetMapping("/{appId}")
    public Object showInfo(@PathVariable int appId){
        App app = appService.get(appId);
        List<AppInstanceVO> instances = appService.findInstances(appId);

        DegradeEndpoint degradeEndpoint = null;
        for (AppInstanceVO instance : instances) {
            try {
                degradeEndpoint = degradeEndpointService.request(instance.getHost(), instance.getPort(), instance.getContextPath());

            } catch(RestClientException e){
                e.printStackTrace();
                continue;
            }
        }

        if(degradeEndpoint != null){
            List<DegradeEndpoint.DegradeItem> degradeInfos = degradeEndpoint.getDegradeInfos();

            HttpHeaders headers = new HttpHeaders();
            headers.set(DegradeConsts.HEADER_AUTH,DegradeConsts.TOKEN);

            ResponseEntity<NamespaceConfig> responseEntity = restTemplate.exchange(String.format(DegradeConsts.CONF_GET_RELEASE_URL,envService.getConfigPortablUrl(app.getEnv())),
                    HttpMethod.GET,
                    new HttpEntity(headers),
                    NamespaceConfig.class,
                    degradeEndpoint.getEnv(),
                    degradeEndpoint.getAppId(),
                    DegradeConsts.CLUSTER_DEFAULT,
                    DegradeConsts.NAMESPACE_DEGRADE);
            Map<String, String> config = Optional.ofNullable(responseEntity.getBody()).map(NamespaceConfig::getConfigurations).orElse(Maps.newHashMap());//jdk8安全访问

            degradeInfos.stream().forEach(item -> {
                if(config.containsKey(item.getKey()) && DegradeConsts.OPEN.equals(config.get(item.getKey()))){
                    item.setOpen(true);
                }
            });

            return degradeInfos;
        }
        return null;
    }

    @PostMapping("/{appId}")
    public Object showInfo(@PathVariable int appId, @RequestBody Map<String,Boolean> config, @UserSession UserInfo userInfo){
        App app = appService.get(appId);
        List<AppInstanceVO> instances = appService.findInstances(appId);

        //记录降级操作日志
        long logId = IdCenter.getInstance().getId();
        OperateLog operateLog = new OperateLog();
        operateLog.setType(OperateType.DEGRADE.getCode());
        operateLog.setServiceId(app.getId());
        operateLog.setServiceName(app.getName());
        operateLog.setLogId(String.valueOf(logId));
        operateLog.setCreateUserid(userInfo.getId());
        operateLog.setCreateUsername(userInfo.getUsername());
        operateLog.setCreateTime(new Date());
        operateLogService.save(operateLog);

        ShellLog shellLog = new ShellLog();
        shellLog.setId(logId);
        shellLog.setCreateTime(new Date());
        StringBuffer stringBuffer = new StringBuffer("新的配置：\r\n");
        stringBuffer.append(JSON.toJSONString(config, SerializerFeature.PrettyFormat));
        stringBuffer.append("\r\n");

        DegradeEndpoint degradeEndpoint = null;
        for (AppInstanceVO instance : instances) {
            try {
                degradeEndpoint = degradeEndpointService.request(instance.getHost(), instance.getPort(), instance.getContextPath());
            } catch(RestClientException e){
                e.printStackTrace();
                continue;
            }
        }


        if(degradeEndpoint != null){
            List<DegradeEndpoint.DegradeItem> degradeInfos = degradeEndpoint.getDegradeInfos();
            stringBuffer.append("查询成功，开始请求配置中心...\r\n");

            HttpHeaders headers = new HttpHeaders();
            headers.set(DegradeConsts.HEADER_AUTH,DegradeConsts.TOKEN);
            ResponseEntity<NamespaceConfig> responseEntity = restTemplate.exchange(String.format(DegradeConsts.CONF_GET_SNAPSHOT_URL,envService.getConfigPortablUrl(app.getEnv())),
                    HttpMethod.GET,
                    new HttpEntity(headers),
                    NamespaceConfig.class,
                    degradeEndpoint.getEnv(),
                    degradeEndpoint.getAppId(),
                    DegradeConsts.CLUSTER_DEFAULT,
                    DegradeConsts.NAMESPACE_DEGRADE);
            Map<String, NamespaceConfig.ConfItem> confItemMap = Optional.ofNullable(responseEntity.getBody()).
                    map(NamespaceConfig::getItems).
                    orElse(Lists.newArrayList())
                    .stream()
                    .collect(Collectors.toMap(NamespaceConfig.ConfItem::getKey, Function.identity()));

            final String env = degradeEndpoint.getEnv();
            final String aoplloAppId = degradeEndpoint.getAppId();
            config.keySet().parallelStream().forEach(k -> {
                if(confItemMap.containsKey(k)){
                    //执行更新操作
                    ItemRequesetDTO item = new ItemRequesetDTO(k,config.get(k) ? DegradeConsts.OPEN : DegradeConsts.CLOSED,confItemMap.get(k).getComment());
                    ResponseEntity<String> response = restTemplate.exchange(String.format(DegradeConsts.CONF_UPDATE_URL,envService.getConfigPortablUrl(app.getEnv())),
                            HttpMethod.PUT,
                            new HttpEntity(item,headers),
                            String.class,
                            env,
                            aoplloAppId,
                            DegradeConsts.CLUSTER_DEFAULT,
                            DegradeConsts.NAMESPACE_DEGRADE,
                            k);
                    stringBuffer.append("配置更新："+k+"执行结果："+response.getStatusCodeValue()+","+response.getBody()+"\r\n");
                }else{
                    //执行新增操作
                    ItemRequesetDTO item = new ItemRequesetDTO(k,config.get(k) ? DegradeConsts.OPEN : DegradeConsts.CLOSED,"");
                    ResponseEntity<String> response = restTemplate.exchange(String.format(DegradeConsts.CONF_ADD_URL,envService.getConfigPortablUrl(app.getEnv())),
                            HttpMethod.POST,
                            new HttpEntity(item,headers),
                            String.class,
                            env,
                            aoplloAppId,
                            DegradeConsts.CLUSTER_DEFAULT,
                            DegradeConsts.NAMESPACE_DEGRADE);
                    stringBuffer.append("配置新增："+k+"执行结果："+response.getStatusCodeValue()+","+response.getBody()+"\r\n");
                }
            });
            //最后发布版本

            ReleaseApplyDTO releaseApplyDTO = new ReleaseApplyDTO(generateReleaseTitle(),generateReleaseComment(userInfo,logId));
            ResponseEntity<String> response = restTemplate.exchange(String.format(DegradeConsts.CONF_APPLY_URL,envService.getConfigPortablUrl(app.getEnv())),
                    HttpMethod.POST,
                    new HttpEntity(releaseApplyDTO,headers),
                    String.class,
                    env,
                    aoplloAppId,
                    DegradeConsts.CLUSTER_DEFAULT,
                    DegradeConsts.NAMESPACE_DEGRADE);
            stringBuffer.append("配置发布结果："+response.getStatusCodeValue()+","+response.getBody()+"\r\n");

            shellLog.setStatus(response.getStatusCodeValue() == 200 ? ExecResult.SUCCESS : ExecResult.ERROR);
            shellLog.setContent(stringBuffer.toString());
            shellLogService.save(shellLog);

            return CommonResult.SUCCESS;

        }
        return CommonResult.SERVICE_INTERNAL_ERROR;
    }


    private String generateReleaseTitle(){
        return DateFormatUtil.NUMBER_FORMAT.format(System.currentTimeMillis())+"-morphling-release";
    }
    private String generateReleaseComment(UserInfo userInfo,long logId){
        return "发布人："+userInfo.getUsername()+",morphling系统日志ID："+logId;
    }
}
