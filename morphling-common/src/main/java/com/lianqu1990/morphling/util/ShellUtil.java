package com.lianqu1990.morphling.util;

import com.lianqu1990.morphling.bean.ExecResult;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class ShellUtil {

    /**
     * @param logId 回显id
     * @param shell
     * @param args
     * @return
     */
    public static ExecResult exec(String logId, String shell,EchoFunction echoFunction,Map<String, String> args,String ...command){
        ExecResult result = new ExecResult();
        StringBuilder buffer = new StringBuilder();
        log.info("exec shell: {},command: {}",shell, Arrays.toString(command));
        ProcessBuilder pb;
        if(command.length == 0){
            pb = new ProcessBuilder(shell);
        }else{
            List<String> commandList = new ArrayList();
            commandList.add(shell);
            for (String cm : command) {
                commandList.add(cm);
            }
            pb = new ProcessBuilder(commandList);
        }
        Map<String, String> sysenv = pb.environment();
        if (args != null) {
            sysenv.putAll(args);
        }

        pb.redirectErrorStream(true);

        Process process = null;
        BufferedReader br = null;
        try {
            process = pb.start();
            InputStream in = process.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line+ "\r\n");
                log.info("==> "+line);
                if(echoFunction != null){
                    echoFunction.print(logId,line);
                }
            }
            in.close();
        } catch (IOException e) {
            log.error("exec error: ",e);
        } finally {
            try {
                if(br != null){
                    br.close();
                }
            } catch (IOException e) {
                log.error("exec error: ",e);
            }
            if(process != null){
                process.destroy();
                try{
                    try {
                        result.setStatus(String.valueOf(process.waitFor()));
                    } catch (InterruptedException e) {
                        result.setStatus(ExecResult.ERROR);
                        log.error("exec error: ",e);
                    }
                }catch (IllegalThreadStateException e) {//shell 脚本缺少exit
                    result.setStatus(ExecResult.SUCCESS);
                    log.error("exec error: ",e);
                }
            }else{
                result.setStatus(ExecResult.ERROR);
            }

            result.setResult(buffer.toString());
        }
        log.info("shell "+shell+" exec result "+result.getStatus());
        echoFunction.close(logId);
        return result;
    }


}
