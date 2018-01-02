package com.lianqu1990.common.utils.proxy;

import java.util.Map;

/**
 * @author hanchao
 * @date 2017/9/12 22:15
 */
public class AsmInterFaceHandler implements InterfaceHandler {
    public static final String PROXY_SUFFIX = "AutoProxy";
    @Override
    public Class<?> generateClass(Class<?> inter,Map<Class,String> resultMapping) throws Exception{
        String name = inter.getSimpleName();
        return null;
    }
}
