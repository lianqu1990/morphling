package com.lianqu1990.springboot.web.register;

/**
 * @author hanchao
 * @date 2017/9/18 13:11
 */
public interface WebRegister {

    boolean regist();

    boolean unregister();

    /**
     * 返回目前注册状态，
     * 对于不区分pause和regist的，无需返回pause状态
     * @return
     */
    int state();

    default boolean pause(){
        return unregister();
    }

    default boolean resume(){
        return regist();
    }



}
