package com.huatu.morphling.web.controller;

import com.huatu.morphling.common.bean.AppInstanceVO;
import com.huatu.morphling.common.consts.RegistConsts;
import com.huatu.morphling.service.local.AppService;
import com.huatu.morphling.service.remote.endpoint.WebRegisterEndPointService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

import static com.huatu.morphling.common.consts.RegistConsts.WEB_REGISTER_URL_TPL;

/**
 * @author hanchao
 * @date 2017/12/7 17:10
 */
public class RegistController {
    @Autowired
    protected AppService appService;
    @Autowired
    private WebRegisterEndPointService webRegisterEndPointService;
    @Autowired
    protected OkHttpClient okHttpClient;

    @PostMapping(value = "/unRegist",params = "type=1")
    public Object unRegistWeb(int id) {
        AppInstanceVO instance = appService.findInstanceByInstanceId(id);
        String url = String.format(WEB_REGISTER_URL_TPL,instance.getHost(),instance.getPort(),instance.getContextPath(), RegistConsts.ACTION_PAUSE);
        RequestBody requestBody = RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE), "");
        Response response = null;
        try {
            response = okHttpClient.newCall(new Request.Builder().url(url.replace("//","/")).post(requestBody).build()).execute();
            return Boolean.parseBoolean(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @PostMapping(value = "/regist",params = "type=1")
    public Object registWeb(int id) {
        AppInstanceVO instance = appService.findInstanceByInstanceId(id);
        Integer response = webRegisterEndPointService.request(instance.getHost(), instance.getPort(), instance.getContextPath());
        String action;
        if(response == RegistConsts.RegistState.INIT){
            action = RegistConsts.ACTION_REG;
        }else{
            //pause状态下或者别的，都可以调用resume,resume要保证幂等
            action = RegistConsts.ACTION_RESUME;
        }

        String url = String.format(WEB_REGISTER_URL_TPL,instance.getHost(),instance.getPort(),instance.getContextPath(), action);
        RequestBody requestBody = RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE), "");
        Response result = null;
        try {
            result = okHttpClient.newCall(new Request.Builder().url(url.replace("//","/")).post(requestBody).build()).execute();
            return Boolean.parseBoolean(result.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    @PostMapping(value = "/regist",params = "type=2")
    public Object registService(int id){
        //TODO
        return null;
    }

    @PostMapping(value = "/unRegister",params = "type=2")
    public Object unRegistService(int id){
        return null;
    }


}
