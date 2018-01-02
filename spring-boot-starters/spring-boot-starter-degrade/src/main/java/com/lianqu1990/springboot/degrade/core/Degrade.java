package com.lianqu1990.springboot.degrade.core;

import java.lang.annotation.*;

/**
 * 只针对方法级别做降级
 * @author hanchao
 * @date 2017/10/16 16:56
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Degrade {
    String key();
    String name();
    String method() default "";
}
