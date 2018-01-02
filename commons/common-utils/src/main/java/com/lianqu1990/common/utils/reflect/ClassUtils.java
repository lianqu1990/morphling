package com.lianqu1990.common.utils.reflect;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther hanchao
 * @date 2016/12/5 17:05
 */
public class ClassUtils {
    public static final int STATIC_MODIFIER = 0x08;
    public static final int PUBLIC_MODIFIER = 0x01;
    private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    public static String getClassByteName(Class clazz) {
        String name = clazz.getCanonicalName();
        return getClassByteName(name);
    }

    public static String getClassByteName(String name) {
        return name.replace(".", "/");
    }

    public static String convertType2Signature(String typeName) {
        return getClassByteName(typeName).replace(">", ";>").replace("<", "<L");
    }


    /**
     * spring的beannamegenarator
     *
     * @param name
     * @return
     */
    public static String makeBeanName(String name) {
        if (name == null || name.length() == 0) {
            return name;
        } else {
            return name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
        }
    }

    /**
     * 最简获取bean属性，要比beanmap快三倍左右，适合无继承关系的简单vo使用
     *
     * @param object
     * @return
     */
    public static Map<String, Object> getBeanProperties(Object object) {
        return getBeanProperties(object,false,false);
    }

    //修饰符表
    /**
     PUBLIC: 1
     PRIVATE: 2
     PROTECTED: 4
     STATIC: 8
     FINAL: 16
     SYNCHRONIZED: 32
     VOLATILE: 64
     TRANSIENT: 128
     NATIVE: 256
     INTERFACE: 512
     ABSTRACT: 1024
     STRICT: 2048
     */

    /**
     * @param object
     * @param staticModifier 是否获取被static修饰的
     * @return
     */
    public static Map<String, Object> getBeanProperties(Object object,boolean declared, boolean staticModifier) {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = object.getClass();
        if (clazz.isPrimitive() || clazz.isArray() || clazz.isEnum() || Collection.class.isAssignableFrom(clazz) || CharSequence.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz)) {
            logger.error("not a bean");
            return map;
        }
        Field[] fields = clazz.getDeclaredFields();
        if(!declared){
            ArrayUtils.addAll(fields,clazz.getFields());
        }
        for (Field field : fields) {
            Class fieldClass = field.getType();
            if (fieldClass.isPrimitive() || CharSequence.class.isAssignableFrom(fieldClass) || Number.class.isAssignableFrom(fieldClass)) {
                int modifiers = field.getModifiers();
                if (!staticModifier && (modifiers & STATIC_MODIFIER) == STATIC_MODIFIER) {
                    continue;
                }
                String name = field.getName();
                Method method;
                try {
                    if ((modifiers & PUBLIC_MODIFIER) == PUBLIC_MODIFIER) {
                        map.put(name,field.get(object));
                        continue;
                    }
                    if (fieldClass.equals(Boolean.class)) {
                        method = clazz.getMethod("is" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()));
                    } else {
                        method = clazz.getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()));
                    }
                    Object value = method.invoke(object);
                    map.put(name, value);
                } catch (Exception e) {
                    logger.error("cant find get method for {}.{}", clazz, name);
                }
            }
        }
        return map;
    }



}
