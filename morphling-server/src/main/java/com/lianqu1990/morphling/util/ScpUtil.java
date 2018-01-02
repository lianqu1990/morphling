package com.lianqu1990.morphling.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPOutputStream;
import com.lianqu1990.morphling.bean.ExecResult;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author hanchao
 * @date 2017/11/20 17:00
 */
@Slf4j
public class ScpUtil {
    public static ExecResult copy(String logId,EchoFunction echoFunction,String source, String targetDirectory, String host, int port, String username, String password) throws IOException {
        StringBuffer bufferLog = new StringBuffer();
        Connection connection = new Connection(host, port);
        connection.connect();
        boolean isAuthed = connection.authenticateWithPassword(username,password);
        if(!isAuthed){
            throw new IllegalArgumentException("authenticate error...");
        }
        File sourceFile = new File(source);
        String log = ">>> begin copy file to "+ host +",path "+targetDirectory+File.separatorChar+sourceFile.getName()+"...";
        log(logId,log,echoFunction,bufferLog);

        SCPClient scpClient = connection.createSCPClient();
        SCPOutputStream scpOutputStream = scpClient.put(sourceFile.getName(), sourceFile.length(), targetDirectory, null);
        FileInputStream inputStream = new FileInputStream(sourceFile);
        int i;
        byte [] bytes = new byte[1024];
        while((i = inputStream.read(bytes)) != -1){
            scpOutputStream.write(bytes,0,i);
        }
        try {
            scpOutputStream.flush();
            scpOutputStream.close();
            inputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        log(logId,">>> copy file over ...",echoFunction,bufferLog);

        return new ExecResult(ExecResult.SUCCESS,log);
    }

    private static void log(String logId,String log,EchoFunction echoFunction,StringBuffer stringBuffer){
        echoFunction.print(logId,log+"\r\n");
        stringBuffer.append(log+"\r\n");
    }
}
