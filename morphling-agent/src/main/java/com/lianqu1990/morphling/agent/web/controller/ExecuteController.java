package com.lianqu1990.morphling.agent.web.controller;

import com.lianqu1990.morphling.agent.util.HttpEchoFunction;
import com.lianqu1990.morphling.bean.ExecResult;
import com.lianqu1990.morphling.util.ShellUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.lianqu1990.morphling.consts.DeployConst.AGENT_COMMAND_DIR;

/**
 * @author hanchao
 * @date 2017/12/15 17:43
 */
@RestController
@RequestMapping("/execute")
@Slf4j
public class ExecuteController {
    @GetMapping("/deploy")
    public void deploy(String action, String project, String tag,HttpServletResponse response) throws IOException {
        Map<String,String> params = new HashMap<>();
        params.put("project_name",project);
        params.put("tag",tag);
        ExecResult execResult = ShellUtil.exec(null, AGENT_COMMAND_DIR + "deploy.sh", new HttpEchoFunction(response), params, action);
        log.info("deploy result : {}",execResult);
        response.getWriter().close();
    }

}
