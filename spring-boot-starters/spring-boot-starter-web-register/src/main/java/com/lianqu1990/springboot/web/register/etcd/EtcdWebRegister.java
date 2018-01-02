package com.lianqu1990.springboot.web.register.etcd;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.net.HttpHeaders;
import com.lianqu1990.common.utils.encode.CharsetConsts;
import com.lianqu1990.springboot.web.register.WebRegister;
import com.lianqu1990.springboot.web.register.core.RegistState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hanchao
 * @date 2017/9/18 13:31
 */
@Slf4j
public class EtcdWebRegister implements WebRegister {
    private static int TTL = 120;//默认节点有效时间
    private static int POLLING = 60; //默认续约间隔时间
    private static final String PATH_PRE = "/v2/keys/";
    private static final String REGISTER_DATA= "{\"host\":\"%s\",\"weight\":%s,\"port\":%s}";


    private String connectString;
    private String host;
    private int port;
    private String serverName;
    private String prefix;


    private String etcdServerHome;//服务注册dir
    private String etcdServerNode;//服务注册路径

    private List<String> etcdServers;

    private static Thread maintainThread;
    private static AtomicBoolean threadInitLock = new AtomicBoolean(false);
    private static volatile boolean running = true;
    private static volatile boolean pausing = false;


    public EtcdWebRegister(String connectString, String host, int port, String serverName, String prefix){
        this.host = host;
        this.port = port;
        this.serverName = serverName;
        this.prefix = prefix;
        this.connectString = connectString;

        this.etcdServerHome = prefix + serverName;
        this.etcdServerNode = etcdServerHome+"/" + host + ":" + port;

        this.etcdServers = Splitter.on(",").splitToList(connectString);
    }


    private boolean doRegist(){
        boolean success = false;
        //因为涉及到续约，所以默认失败则持续请求,最多三次
        for (int i = 0; i < 3; i++) {
            for (String etcdServer : etcdServers) {
                URL url;
                HttpURLConnection connection = null;
                try {
                    url = new URL(etcdServer+PATH_PRE+etcdServerNode);
                    connection = buildEtcdConnection(url,HttpMethod.PUT);

                    Node.Request request = Node.Request.builder()
                            .value(String.format(REGISTER_DATA,host,5,port))
                            .ttl(TTL)
                            .build();

                    OutputStream out = connection.getOutputStream();
                    out.write(buildBody(request).getBytes(CharsetConsts.DEFAULT_CHARSET));
                    out.flush();
                    out.close();
                    InputStream in;
                    if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201){
                        success = true;
                        in = connection.getInputStream();
                    }else{
                        in = connection.getErrorStream();
                    }
                    String response = IOUtils.toString(in);
                    in.close();
                    log.info("request node: {} , result: {} , response: {} ",etcdServer,success,response);
                    if(success){
                        return true;
                    }
                } catch(Exception e){
                    log.error("request node : {} failed...",etcdServer,e);
                } finally {
                    if(connection != null){
                        try {
                            connection.disconnect();
                        } catch(Exception e){
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean doUnRegist(){
        boolean success = false;
        for (String etcdServer : etcdServers) {
            URL url;
            HttpURLConnection connection = null;
            try {
                url = new URL(etcdServer+PATH_PRE+etcdServerNode);
                connection = buildEtcdConnection(url,HttpMethod.DELETE);
                InputStream in;
                if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201){
                    success = true;
                    in = connection.getInputStream();
                }else{
                    in = connection.getErrorStream();
                }
                String response = IOUtils.toString(in);
                in.close();
                log.info("request node: {} , result: {} , response: {} ",etcdServer,success,response);
                if(success){
                    break;
                }
            } catch(Exception e){
                log.error("request node : {} failed...",etcdServer,e);
            } finally {
                if(connection != null){
                    try {
                        connection.disconnect();
                    } catch(Exception e){
                    }
                }
            }
        }
        return success;
    }


    @Override
    public boolean regist() {
        log.info("start register server http(s)://{}:{} to {} .",host,port,etcdServerNode);
        if(threadInitLock.compareAndSet(false,true)){
            maintainThread = new Thread(){
                @Override
                public void run() {
                    for(;running;){
                        try {
                            Thread.sleep(TimeUnit.MILLISECONDS.convert(POLLING, TimeUnit.SECONDS)); // 一分钟续约一次
                            if(running && !pausing) {
                                doRegist();
                            }
                        } catch (InterruptedException e) {
                            log.info("interrupt thread...");
                        }
                    }
                }
            };
            maintainThread.setDaemon(true);
            maintainThread.start();
        }else{
            pausing = false;
        }
        return doRegist();
    }

    @Override
    public boolean unregister() {
        log.info("start unregister the server from etcd. node={}",etcdServerNode);
        running = false;
        maintainThread.interrupt();
        return doUnRegist();
    }

    @Override
    public int state() {
        if(!threadInitLock.get()){
            return RegistState.INIT;
        }
        if(pausing){
            return RegistState.PAUSING;
        }else{
            return RegistState.RUNNING;
        }
    }


    @Override
    public boolean pause() {
        pausing = true;//先设置暂停状态，防止继续注册
        return doUnRegist();
    }

    /**
     * 转换为map消息体
     * @param request
     * @return
     */
    private String buildBody(Node.Request request){
        Map<String,Object> map = (Map) JSON.toJSON(request);
        return Joiner.on("&").withKeyValueSeparator("=").join(map);
    }

    private HttpURLConnection buildEtcdConnection(URL url,HttpMethod method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod(method.toString());
        switch (method){
            case PUT:
            case POST:
                connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
                break;
            default:
                break;
        }

        connection.setConnectTimeout(1000);
        connection.setReadTimeout(5000);
        return connection;
    }

}
