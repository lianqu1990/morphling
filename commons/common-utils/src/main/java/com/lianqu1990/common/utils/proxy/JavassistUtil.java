package com.lianqu1990.common.utils.proxy;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author hanchao
 * @date 2017/9/12 22:24
 */
public class JavassistUtil {

    public static void dynamicImplementsMethodsFromInterface(CtClass implementer, Method method,Map<Class,String> resultMapping)
            throws CannotCompileException {
        String methodCode = generateMethodCode(method,resultMapping);
        CtMethod cm = CtNewMethod.make(methodCode, implementer);
        implementer.addMethod(cm);
    }

    public static String generateMethodCode(Method method, Map<Class, String> resultMapping) {
        String methodName = method.getName();
        String methodReturnType = method.getReturnType().getCanonicalName();
        Class<?>[] parameters = method.getParameterTypes();
        Class<?>[] exceptionTypes = method.getExceptionTypes();
        StringBuffer exceptionBuffer = new StringBuffer();

        // 异常
        if (exceptionTypes.length > 0)
            exceptionBuffer.append(" throws ");
        for (int i = 0; i < exceptionTypes.length; i++) {
            if (i != exceptionTypes.length - 1)
                exceptionBuffer.append(exceptionTypes[i].getName()).append(",");
            else
                exceptionBuffer.append(exceptionTypes[i].getName());
        }

        StringBuffer parameterBuffer = new StringBuffer();
        // 组装方法的参数列表
        for (int i = 0; i < parameters.length; i++) {
            Class<?> parameter = parameters[i];
            String parameterType = parameter.getName();
            // 动态指定方法参数的变量名
            String refName = "a" + i;
            if (i != parameters.length - 1)
                parameterBuffer.append(parameterType).append(" " + refName)
                        .append(",");
            else
                parameterBuffer.append(parameterType).append(" " + refName);
        }

        StringBuffer methodDeclare = new StringBuffer();
        // 方法声明，由于是实现接口的方法，所以是public
        methodDeclare.append("public ").append(methodReturnType).append(" ")
                .append(methodName).append("(").append(parameterBuffer)
                .append(")").append(exceptionBuffer).append(" {\n");

        if (method.getReturnType().isPrimitive()) {
            if (method.getReturnType().equals(Void.TYPE)) {
                methodDeclare.append("return ;\n");
            } else if (method.getReturnType().equals(Boolean.TYPE))
                methodDeclare.append("return false;\n");
            else if (method.getReturnType().equals(Integer.TYPE))
                methodDeclare.append("return -1;\n");
            else if (method.getReturnType().equals(Long.TYPE))
                methodDeclare.append("return -1L;\n");
            else if (method.getReturnType().equals(Float.TYPE))
                methodDeclare
                        .append("return 0f;\n");
            else if (method.getReturnType().equals(Double.TYPE))
                methodDeclare.append("return 0d;\n");
            else if (method.getReturnType().equals(Character.TYPE))
                methodDeclare.append("return 'a';\n");
            else if (method.getReturnType().equals(Byte.TYPE))
                methodDeclare.append("return ((Byte)\"a\").byteValue();\n");
            else if (method.getReturnType().equals(Short.TYPE))
                methodDeclare.append("return 0;\n");
            else {
                methodDeclare.append("return null;\n");
            }
        } else {
            //自定义返回类型，但是必须是代码形式,需要包含包名
            if (resultMapping != null && resultMapping.containsKey(method.getReturnType())) {
                methodDeclare.append("return " + resultMapping.get(method.getReturnType()) + ";\n");
            }
            methodDeclare.append("return null;\n");
        }

        methodDeclare.append("}");

        return methodDeclare.toString();
    }
}
