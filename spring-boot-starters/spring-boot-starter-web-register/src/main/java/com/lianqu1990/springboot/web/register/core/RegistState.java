package com.lianqu1990.springboot.web.register.core;

/**
 * @author hanchao
 * @date 2017/12/6 18:01
 */
public class RegistState {
    public static int INIT = 1;//初始状态，出现在启动不自动注册的情况下
    public static int PAUSING = 2;//暂停
    public static int RUNNING = 3;//正常
}
