package com.lianqu1990.common.utils.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @auther hanchao
 * @date 2016/12/5 16:06
 */
public class MethodUtils {
    public static String getGenericReturnTypeName(Method method){
        Type returnType = method.getGenericReturnType();
        try {
            Method nameMethod = Type.class.getMethod("getTypeName");
            return (String) nameMethod.invoke(returnType);
        } catch(Exception e){
            // <java 8
            return returnType.toString();
        }
    }
}
