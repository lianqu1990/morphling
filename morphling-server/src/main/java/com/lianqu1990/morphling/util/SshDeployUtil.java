package com.lianqu1990.morphling.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPOutputStream;
import ch.ethz.ssh2.Session;
import com.lianqu1990.morphling.bean.ExecResult;
import com.lianqu1990.morphling.consts.DeployConst;
import com.lianqu1990.common.utils.encode.CharsetConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * 类jenkins目前的部署方式
 * @author hanchao
 * @date 2017/11/20 17:00
 */
@Slf4j
public class SshDeployUtil {
    public static ExecResult deploy(String logId,EchoFunction echoFunction,String source, String host, int port, String username, String password,String appName) throws IOException {
        StringBuffer bufferLog = new StringBuffer();
        Connection connection = new Connection(host, port);
        connection.connect();
        boolean isAuthed = connection.authenticateWithPassword(username,password);
        if(!isAuthed){
            throw new IllegalArgumentException("authenticate error...");
        }

        Session session = connection.openSession();
        session.execCommand("[ -d "+DeployConst.PACKAGE_FOLDER+" ] && echo 'find folder' || mkdir -p "+DeployConst.PACKAGE_FOLDER, CharsetConsts.DEFAULT_ENCODING);
        session.close();


        File sourceFile = new File(source);
        String remoteFile = DeployConst.PACKAGE_FOLDER+sourceFile.getName();

        String log = ">>> begin copy file to "+ host +",path "+ remoteFile;
        log(logId,log,echoFunction,bufferLog);

        SCPClient scpClient = connection.createSCPClient();
        SCPOutputStream scpOutputStream = scpClient.put(sourceFile.getName(), sourceFile.length(), DeployConst.PACKAGE_FOLDER, "0644");
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

        session = connection.openSession();
        session.execCommand("tar -zvxf "+remoteFile+" -C /app/");
        //session.execCommand("tail -f /app/logs/course-web-server/course-web-server_inf.log");
        InputStream stdout = session.getStdout();
        dealInputStream(stdout,logId,echoFunction,bufferLog);

        session.close();

        log(logId,">>> decompress file over...",echoFunction,bufferLog);

        session = connection.openSession();
        session.execCommand("export BASH_ENV=/etc/profile ; /app/"+appName+"/server.sh restart");

        log(logId,">>> deploy over...",echoFunction,bufferLog);
        stdout = session.getStdout();
        dealInputStream(stdout,logId,echoFunction,bufferLog);

        session.close();
        connection.close();
        echoFunction.close(logId);
        return new ExecResult(ExecResult.SUCCESS,bufferLog.toString());
    }



    public static ExecResult uninstall(String logId,EchoFunction echoFunction, String host, int port, String username, String password,String appName) throws IOException {
        if(StringUtils.isBlank(appName)){
            //防止直接删除整个文件夹
            throw new IllegalArgumentException("appName cant be null....");
        }
        StringBuffer bufferLog = new StringBuffer();
        Connection connection = new Connection(host, port);
        connection.connect();
        boolean isAuthed = connection.authenticateWithPassword(username,password);
        if(!isAuthed){
            throw new IllegalArgumentException("authenticate error...");
        }

        log(logId,">>> stop server ...",echoFunction,bufferLog);

        Session session = connection.openSession();
        session.execCommand("export BASH_ENV=/etc/profile ; /app/"+appName+"/server.sh stop");
        InputStream stdout = session.getStdout();
        dealInputStream(stdout,logId,echoFunction,bufferLog);
        session.close();

        log(logId,">>> remove folder...",echoFunction,bufferLog);
        session = connection.openSession();
        session.execCommand("rm -rf /app/"+appName+"/");
        stdout = session.getStdout();
        dealInputStream(stdout,logId,echoFunction,bufferLog);

        log(logId,">>> uninstall success...",echoFunction,bufferLog);

        session.close();
        connection.close();
        echoFunction.close(logId);
        return new ExecResult(ExecResult.SUCCESS,bufferLog.toString());
    }

    private static void log(String logId,String logStr,EchoFunction echoFunction,StringBuffer stringBuffer){
        log.info(logStr);
        echoFunction.print(logId,logStr);
        stringBuffer.append(logStr+"\r\n");
    }

    private static void dealInputStream(InputStream inputStream,String logId,EchoFunction echoFunction,StringBuffer stringBuffer) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = bufferedReader.readLine()) != null){
            log(logId,line,echoFunction,stringBuffer);
        }
        try {
            bufferedReader.close();
            inputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args){
        try {
            deploy("1234", new EchoFunction() {
                @Override
                public void print(String logId, String line) {
                    System.out.println(line);
                }
            }, "C:\\Projects\\IdeaProjects\\morphling\\morphling-agent\\target\\morphling-agent.tar.gz","192.168.100.22",9527,"root","huatu!2016@ztk#1zws%22","morphling-agent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
