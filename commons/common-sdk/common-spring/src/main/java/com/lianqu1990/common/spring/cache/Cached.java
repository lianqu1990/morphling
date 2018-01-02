package com.lianqu1990.common.spring.cache;

import java.lang.annotation.*;

/**
 * 用来标记方法中会生成缓存
 * @author hanchao
 * @date 2017/10/5 18:19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cached {

    /**
     * 缓存的业务名称，如“课程列表”
     * @return
     */
    String name();

    String[] key();//spel表达式，

    /**
     * 参数
     * @return
     */
    Param[] params() default {};

    DataScourseType sourceType() default DataScourseType.REDIS;

    int cluster() default 1;//所使用的集群，目前只有1个

    enum DataScourseType{
        REDIS,
        MEMCACHE,
        SSDB,
        GETINSIDE, // 对于本地缓存来说，只需要获取缓存和删除缓存
        DELINSIDE
    }


    @interface Param{
        /**
         * 键的业务名称，如课程id
         * @return
         */
        String name();

        String value(); //参数名

        Class<?> type() default String.class;
    }

    boolean watchable() default true;

    /**
     * 如果不允许外部系统进行更改，设置为false
     * @return
     */
    boolean modifiable() default true;

}
