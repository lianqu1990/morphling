package com.lianqu1990.morphling.spring.resolver;

import java.lang.annotation.*;

/**
 * @author hanchao
 * @date 2017/11/3 14:37
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserSession {

    /**
     * 验证session信息,会调用assert方法,可能会抛出异常
     * @return
     */
    boolean check() default true;

}
