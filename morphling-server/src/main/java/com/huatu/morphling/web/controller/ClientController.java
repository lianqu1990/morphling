package com.huatu.morphling.web.controller;

import com.huatu.common.consts.DataState;
import com.huatu.morphling.bean.ExecResult;
import com.huatu.morphling.bean.UserInfo;
import com.huatu.morphling.common.enums.OperateType;
import com.huatu.morphling.consts.DeployConst;
import com.huatu.morphling.consts.MorphlingResponse;
import com.huatu.morphling.dao.jpa.entity.Client;
import com.huatu.morphling.dao.jpa.entity.OperateLog;
import com.huatu.morphling.dao.jpa.entity.ShellLog;
import com.huatu.morphling.service.local.ClientService;
import com.huatu.morphling.service.local.OperateLogService;
import com.huatu.morphling.service.local.ShellLogService;
import com.huatu.morphling.service.remote.endpoint.HealthEndpointService;
import com.huatu.morphling.spring.resolver.UserSession;
import com.huatu.morphling.util.EchoFunction;
import com.huatu.morphling.util.SshDeployUtil;
import com.huatu.morphling.util.WebsocketEcho;
import com.huatu.morphling.utils.code.IdCenter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hanchao
 * @date 2017/11/8 17:18
 */
@RequestMapping("/client")
@RestController
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private ShellLogService shellLogService;
    @Autowired
    private HealthEndpointService healthEndpointService;
    @Autowired
    @Qualifier("coreThreadPool")
    private TaskExecutor taskExecutor;
    @GetMapping
    public Object list(@CookieValue String env){
        List<Client> clientList = clientService.findByEnv(env);
        final String currentVersion = getClientCurrentVersion();
        clientList.parallelStream().forEach(x -> {
            //是否需要更新
            if(currentVersion.compareTo(x.getPackVersion()) > 0){
                x.setPackVersion("1");
            }else{
                x.setPackVersion("0");
            }
        });
        return clientList;
    }


    @PostMapping
    public Object add(@CookieValue String env,
                      @UserSession UserInfo userInfo,
                      Client client,
                      int serverPort,
                      String username,
                      String password){

        long logId = IdCenter.getInstance().getId();
        client.setEnv(env);
        client.setCreateTime(new Date());
        client.setCreateUserid(userInfo.getId());
        client.setCreateUsername(userInfo.getUsername());
        client.setState(DataState.EFFECTIVE);
        client.setPackVersion(getClientCurrentVersion());
        clientService.save(client);//保存client

        OperateLog operateLog = new OperateLog();
        operateLog.setType(OperateType.CLIENT_INSTALL.getCode());
        operateLog.setServiceId(client.getId());
        operateLog.setServiceName(client.getName());
        operateLog.setCreateTime(new Date());
        operateLog.setCreateUserid(userInfo.getId());
        operateLog.setCreateUsername(userInfo.getUsername());
        operateLog.setLogId(String.valueOf(logId));
        operateLogService.save(operateLog);


        //执行部署动作
        taskExecutor.execute(() -> {
            String log;
            String status;
            Date now = new Date();
            EchoFunction echoFunction = new WebsocketEcho();
            try {
                ExecResult execResult = SshDeployUtil.deploy(String.valueOf(logId), echoFunction, DeployConst.AGENT_SOURCE, client.getHostAddress(), serverPort, username, password, DeployConst.AGENT_APP_NAME);
                log = execResult.getResult();
                status = execResult.getStatus();
            } catch (Exception e) {
                log = ExceptionUtils.getStackTrace(e);
                status = ExecResult.ERROR;
                echoFunction.print(String.valueOf(logId), log);
                echoFunction.close(String.valueOf(logId));
            }
            //入库
            ShellLog shellLog = new ShellLog(logId,status,log,now);
            shellLogService.save(shellLog);
        });

        //返回logid,
        return String.valueOf(logId);
    }


    @PutMapping
    public Object update(@UserSession UserInfo userInfo,
                         int id,
                         @RequestParam int serverPort,
                         @RequestParam String username,
                         @RequestParam String password){
        long logId = IdCenter.getInstance().getId();
        Client client = clientService.get(id);


        OperateLog operateLog = new OperateLog();
        operateLog.setType(OperateType.CLIENT_INSTALL.getCode());
        operateLog.setServiceId(client.getId());
        operateLog.setServiceName(client.getName());
        operateLog.setCreateTime(new Date());
        operateLog.setCreateUserid(userInfo.getId());
        operateLog.setCreateUsername(userInfo.getUsername());
        operateLog.setLogId(String.valueOf(logId));
        operateLogService.save(operateLog);

        //执行卸载动作
        taskExecutor.execute(() -> {
            String log;
            String status;
            Date now = new Date();
            EchoFunction echoFunction = new WebsocketEcho();
            try {
                ExecResult execResult = SshDeployUtil.deploy(String.valueOf(logId), echoFunction, DeployConst.AGENT_SOURCE, client.getHostAddress(), serverPort, username, password, DeployConst.AGENT_APP_NAME);
                if(execResult.getStatus().equals(ExecResult.SUCCESS)){
                    client.setPackVersion(getClientCurrentVersion());
                    clientService.save(client);
                }
                log = execResult.getResult();
                status = execResult.getStatus();
            } catch (Exception e) {
                log = ExceptionUtils.getStackTrace(e);
                status = ExecResult.ERROR;
                echoFunction.print(String.valueOf(logId), log);
                echoFunction.close(String.valueOf(logId));
            }
            //入库
            ShellLog shellLog = new ShellLog(logId,status,log,now);
            shellLogService.save(shellLog);
        });

        //返回logid,
        return String.valueOf(logId);
    }


    @DeleteMapping
    public Object remove(@UserSession UserInfo userInfo,
                         int id,
                         @RequestParam int serverPort,
                         @RequestParam String username,
                         @RequestParam String password){
        long logId = IdCenter.getInstance().getId();
        Client client = clientService.get(id);
        clientService.delete(client);

        OperateLog operateLog = new OperateLog();
        operateLog.setType(OperateType.CLIENT_INSTALL.getCode());
        operateLog.setServiceId(client.getId());
        operateLog.setServiceName(client.getName());
        operateLog.setCreateTime(new Date());
        operateLog.setCreateUserid(userInfo.getId());
        operateLog.setCreateUsername(userInfo.getUsername());
        operateLog.setLogId(String.valueOf(logId));
        operateLogService.save(operateLog);

        //执行卸载动作
        taskExecutor.execute(() -> {
            String log;
            String status;
            Date now = new Date();
            EchoFunction echoFunction = new WebsocketEcho();
            try {
                ExecResult execResult = SshDeployUtil.uninstall(String.valueOf(logId), echoFunction, client.getHostAddress(), serverPort, username, password, DeployConst.AGENT_APP_NAME);
                log = execResult.getResult();
                status = execResult.getStatus();
            } catch (Exception e) {
                log = ExceptionUtils.getStackTrace(e);
                status = ExecResult.ERROR;
                echoFunction.print(String.valueOf(logId), log);
                echoFunction.close(String.valueOf(logId));
            }
            //入库
            ShellLog shellLog = new ShellLog(logId,status,log,now);
            shellLogService.save(shellLog);
        });

        //返回logid,
        return String.valueOf(logId);
    }


    @GetMapping("/health")
    public Object checkClient(String host, int port){
        try {
            Map response = healthEndpointService.request(host, port,"");
            return response;
        } catch(Exception e){
            return MorphlingResponse.CONNECTION_ERROR;
        }
    }



    private static String getClientCurrentVersion(){
        File file = new File(DeployConst.AGENT_SOURCE);
        return String.valueOf(file.lastModified()/1000);
    }
}
