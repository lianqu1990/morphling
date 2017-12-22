package com.huatu.morphling.web.controller;

import com.google.common.collect.Lists;
import com.huatu.common.exception.BizException;
import com.huatu.morphling.bean.ExecResult;
import com.huatu.morphling.bean.UserInfo;
import com.huatu.morphling.common.bean.AppInstanceVO;
import com.huatu.morphling.common.enums.InstanceStatus;
import com.huatu.morphling.common.enums.OperateType;
import com.huatu.morphling.consts.DeployConst;
import com.huatu.morphling.consts.MorphlingResponse;
import com.huatu.morphling.dao.jpa.entity.*;
import com.huatu.morphling.service.local.*;
import com.huatu.morphling.spring.resolver.UserSession;
import com.huatu.morphling.util.EchoFunction;
import com.huatu.morphling.util.GitUtil;
import com.huatu.morphling.util.ShellUtil;
import com.huatu.morphling.util.WebsocketEcho;
import com.huatu.morphling.utils.code.IdCenter;
import com.huatu.morphling.utils.collection.HashMapBuilder;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static com.huatu.morphling.consts.DeployConst.*;
import static com.huatu.morphling.consts.MorphlingResponse.NO_PACKAGE;

/**
 * @author hanchao
 * @date 2017/12/7 16:51
 */
@RestController
@RequestMapping("/deploy")
public class DeployController extends RegistController{

    @Autowired
    private AppService appService;

    @Autowired
    private AppInstanceService appInstanceService;

    @Autowired
    private EnvService envService;

    @Autowired
    private OperateLogService operateLogService;

    @Autowired
    private ShellLogService shellLogService;

    @Autowired
    @Qualifier("coreThreadPool")
    private TaskExecutor taskExecutor;

    @PostMapping
    public Object deploy(int appId,
                         String action,
                         @RequestParam("instanceIds") List<Integer> instanceIds,
                         @RequestParam(value = "parallel",defaultValue = "false") boolean parallel,
                         @UserSession UserInfo userInfo){
        App app = appService.get(appId);
        List<AppInstanceVO> instances = appService.findInstances(instanceIds);

        Env env = envService.getEnvs().get(app.getEnv());
        if(env.isProd()){
            //TODO 策略控制
        }

        if(StringUtils.isBlank(app.getCurrentPackVersion())){
            throw new BizException(NO_PACKAGE);
        }

        long logId = IdCenter.getInstance().getId();

        List<String> deployClients = Lists.newArrayList();
        for (AppInstanceVO instance : instances) {
            deployClients.add(instance.getClientName());

            AppInstance appInstance = appInstanceService.get(instance.getId());
            switch (action.toLowerCase()){
                case "deploy":
                    appInstance.setStatus(InstanceStatus.RUNNING.getCode());
                    appInstance.setCurrentVersion(app.getCurrentPackVersion());
                    break;
                case "start":
                case "restart":
                    appInstance.setStatus(InstanceStatus.RUNNING.getCode());
                    break;
                case "stop":
                    appInstance.setStatus(InstanceStatus.STOPPED.getCode());
                    break;
            }
            appInstanceService.save(appInstance);
        }

        OperateLog operateLog = new OperateLog();
        operateLog.setType("deploy".equalsIgnoreCase(action) ? OperateType.APP_DEPLOY.getCode() : OperateType.APP_CONTROL.getCode());
        operateLog.setServiceId(appId);
        operateLog.setServiceName(app.getName()+"  【"+StringUtils.join(deployClients,",")+"】");
        operateLog.setCreateTime(new Date());
        operateLog.setCreateUserid(userInfo.getId());
        operateLog.setCreateUsername(userInfo.getUsername());
        operateLog.setLogId(String.valueOf(logId));
        operateLogService.save(operateLog);


        //执行操作
        final EchoFunction echoFunction = new WebsocketEcho();
        taskExecutor.execute(() -> {
            Stream<AppInstanceVO> stream = parallel ? instances.parallelStream() : instances.stream();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.authenticator((route,response) -> {
                String credential = Credentials.basic(AGENT_USERNAME, AGENT_PASSWORD);
                return response.request().newBuilder().header("Authorization", credential).build();
            }).connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .writeTimeout(5000,TimeUnit.MILLISECONDS)
                    .readTimeout(60000,TimeUnit.MILLISECONDS);
            OkHttpClient client = builder.build();
            StringBuffer stringBuffer = new StringBuffer();
            AtomicBoolean success = new AtomicBoolean(true);
            stream.forEach(instance -> {
                String url = "http://"+instance.getHost()+":"+instance.getClientPort()+"/execute/deploy?action="+action+"&project="+app.getName()+"&tag="+app.getCurrentPackVersion();
                try {
                    Response response = client.newCall(new Request.Builder().url(url).get().build()).execute();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    String line;
                    while((line = bufferedReader.readLine()) != null){
                        String lineDec = instance.getHost()+"：     "+line;
                        echoFunction.print(String.valueOf(logId),lineDec);
                        stringBuffer.append(lineDec+"\r\n");
                    }
                    bufferedReader.close();
                    success.set(success.get() && true);
                } catch (IOException e) {
                    success.set(success.get() && false);
                    e.printStackTrace();
                }
            });
            ShellLog shellLog = new ShellLog(logId,success.get() ? ExecResult.SUCCESS:ExecResult.ERROR,stringBuffer.toString(),new Date());
            shellLogService.save(shellLog);
            echoFunction.close(String.valueOf(logId));
        });
        return String.valueOf(logId);
    }


    @GetMapping("/package")
    public Object packageApplication(@RequestParam int appId,
                                     @RequestParam(value = "tag",required = false)String tag,
                                     @UserSession UserInfo userInfo){
        App app = appService.get(appId);
        Env env = envService.getEnvs().get(app.getEnv());
        if(env.isProd() && StringUtils.isBlank(tag)){
            throw new BizException(MorphlingResponse.PACKAGE_BRANCH);
        }
        if(StringUtils.isBlank(tag)){
            tag = StringUtils.isNotBlank(app.getGitBranch()) ? app.getGitBranch() : app.getEnv();
        }

        long logId = IdCenter.getInstance().getId();
        OperateLog operateLog = new OperateLog();
        operateLog.setType(OperateType.APP_PACKAGE.getCode());
        operateLog.setServiceId(app.getId());
        operateLog.setServiceName(app.getName());
        operateLog.setCreateTime(new Date());
        operateLog.setCreateUserid(userInfo.getId());
        operateLog.setCreateUsername(userInfo.getUsername());
        operateLog.setLogId(String.valueOf(logId));
        operateLogService.save(operateLog);

        final String gitBranch = tag;

        //执行部署动作
        taskExecutor.execute(() -> {
            String log;
            String status;
            Date now = new Date();
            EchoFunction echoFunction = new WebsocketEcho();
            try {
                Map<String,String> params = HashMapBuilder.newBuilder()
                        .put("project_name",app.getName())
                        .put("git_url",app.getGitUrl())
                        .put("git_branch",gitBranch)
                        .put("module",app.getMvnModule())
                        .put("profile",app.getEnv())
                        .buildUnsafe();
                ExecResult execResult = ShellUtil.exec(String.valueOf(logId),DeployConst.COMMAND_DIR+"package.sh",echoFunction,params);
                if(execResult.getStatus().equals(ExecResult.SUCCESS)){
                    app.setCurrentPackVersion(gitBranch);
                    appService.save(app);
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


    @GetMapping("/{appId}/tags")
    public Object listTags(@PathVariable int appId) throws IOException, GitAPIException {
        App app = appService.get(appId);
        String workspace = WORKSPACE+app.getName();
        return GitUtil.listGitTags(workspace,app.getGitUrl());
    }

}
