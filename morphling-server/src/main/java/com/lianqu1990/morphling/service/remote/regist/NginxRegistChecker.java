package com.lianqu1990.morphling.service.remote.regist;

import com.lianqu1990.morphling.dao.jpa.entity.Env;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 后期更换confd可以直接修改此类，检测比较简单，直接head请求即可
 * @author hanchao
 * @date 2017/12/7 11:14
 */
@Service
public class NginxRegistChecker implements RegistChecker {
    private static final String PATH_PRE = "/v2/keys/ztk-servers/";

    private static final String URL_TEMPALTE = "%s"+PATH_PRE+"%s/%s:%d";

    @Autowired
    private OkHttpClient okHttpClient;

    @Override
    public boolean resolve(int type) {
        return type == 1;
    }

    @Override
    public boolean health(String serviceName, String host, int port, Env.EnvProperties envProperties) {
        List<String> checkServers = envProperties.getEtcds();
        for (String checkServer : checkServers) {
            String url = String.format(URL_TEMPALTE,checkServer,serviceName,host,port);
            try {
                Response response = okHttpClient.newCall(new Request.Builder().url(url).head().build()).execute();
                if(response.code() == HttpStatus.NOT_FOUND.value()){
                    return false;
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
