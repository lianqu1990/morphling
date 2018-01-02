package com.lianqu1990.springboot.web.tools.interceptor;

import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 临时放在这里，后期要放到单独的第三方网关中
 * 用户参数认证
 * Created by shaojieyue
 * Created time 2016-11-24 11:33
 */
@Deprecated
public class AuthenticationHandlerInterceptor extends HandlerInterceptorAdapter {

    private static Map<String, String> users = new HashMap<String, String>();
    static {
        users.put("huatuibm","79asjov1ydn8uilew5krmxqz6gtp20hbc4f3");
        users.put("huatuwx","6tHNjGHibFQ7lWOkAPs4ELBtNwKneBG21z2E");
        users.put("huatuztk","DAyONsVGdrK7zHEsxJVfkpXRsG7890wbnQeI");
        users.put("huatuxuexi","0cdnVkA9yihTdqPWdm11G3AGgW4gWh7CywDy");
    }

    /**
     * This implementation always returns {@code true}.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //用户id
        final String user = request.getHeader("user");
        if (user == null) {
            //否则设置签名失败
            response.setStatus(401);
            return false;
        }
        //签名秘钥
        final String secret = users.get(user);
        if (secret == null) {
            //否则设置签名失败
            response.setStatus(401);
            return false;
        }

        //签名后的值
        final String sig = request.getHeader("sig");

        final Map<String, String[]> parameterMap = request.getParameterMap();
        //以secret字符串开头，追加排序后参数名称和值，格式：secretkey1value1key2value2.
        StringBuffer original = new StringBuffer(secret);
        final String[] keys = parameterMap.keySet().stream().toArray(String[]::new);
        Arrays.sort(keys);//对key进行生序排序
        for (String key : keys) {
            original.append(key);
            //value数组进行连接 value1value2
            original.append(StringUtils.join(parameterMap.get(key)));
        }

        //本地生成签名
        final String mySig = HmacUtils.hmacSha1Hex(secret, original.toString());
        if (mySig.equals(sig)) {
            return true;
        }

        //否则设置签名失败
        response.setStatus(401);
        return false;
//        return true;
    }

}
