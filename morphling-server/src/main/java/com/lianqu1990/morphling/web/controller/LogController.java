package com.lianqu1990.morphling.web.controller;

import com.google.common.collect.Lists;
import com.lianqu1990.morphling.common.enums.OperateType;
import com.lianqu1990.morphling.dao.jpa.entity.ShellLog;
import com.lianqu1990.morphling.service.local.OperateLogService;
import com.lianqu1990.morphling.service.local.ShellLogService;
import com.lianqu1990.common.utils.date.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author hanchao
 * @date 2017/12/19 9:52
 */
@RequestMapping("/logs")
@Controller
public class LogController {
    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private ShellLogService shellLogService;

    @RequestMapping("/deploy")
    @ResponseBody
    public Object getDeployLogs(int appId, int start){
        return operateLogService.findByTypeAndServiceId(Lists.<Byte>newArrayList(OperateType.APP_PACKAGE.getCode(),OperateType.APP_DEPLOY.getCode(),OperateType.APP_CONTROL.getCode()),appId,start);
    }


    @RequestMapping("/show/{logId}")
    public void showLog(@PathVariable long logId, HttpServletResponse response) throws IOException {
        ShellLog shellLog = shellLogService.get(logId);
        PrintWriter writer = response.getWriter();
        if(shellLog != null){
            writer.println("执行时间："+DateFormatUtil.DEFAULT_FORMAT.format(shellLog.getCreateTime()));
            writer.println("执行结果："+shellLog.getStatus());
            writer.println(shellLog.getContent());
            writer.flush();
            writer.close();
        }
    }
}
