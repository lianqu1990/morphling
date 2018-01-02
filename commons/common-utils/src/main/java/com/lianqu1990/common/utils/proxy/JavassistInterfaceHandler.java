package com.lianqu1990.common.utils.proxy;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hanchao
 * @date 2017/9/12 22:23
 */
@Slf4j
public class JavassistInterfaceHandler implements InterfaceHandler {
    private static ClassPool cp;
    private AtomicBoolean initLock = new AtomicBoolean(true);
    public static final String PROXY_SUFFIX = "AutoProxy";


    @Override
    public Class<?> generateClass(Class<?> inter, Map<Class,String> resultMapping) throws Exception {
        if(cp == null){
            if(initLock.compareAndSet(true,false)){
                cp = new ClassPool();
                cp.appendSystemPath();
                cp.insertClassPath(new ClassClassPath(JavassistInterfaceHandler.class));
            }
        }
        String interfaceName = inter.getName();
        String interfaceNamePath = interfaceName;
        CtClass ctInterface = cp.getCtClass(interfaceNamePath);
        String implClass = interfaceNamePath + PROXY_SUFFIX;
        CtClass cc = cp.makeClass(implClass);

        Method[] methods = inter.getMethods();

        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            log.debug(" generate method " + method.getName());
            JavassistUtil.dynamicImplementsMethodsFromInterface(cc, method,resultMapping);
        }
        writeCtFile(cc);
        return cc.toClass();
    }


    private static void writeCtFile(CtClass ctClass) {
        try {
            //ctClass.writeFile(new File(ProxyFactroy.class.getResource(".").getFile()).getAbsolutePath().replaceAll(packageName, ""));
            ctClass.writeFile(new File("/tmp/.classes/javassist/").getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
