package com.lianqu1990.morphling.common.consts;

/**
 * @author hanchao
 * @date 2017/12/7 17:08
 */
public class RegistConsts {
    public static final String WEB_REGISTER_URL_TPL = "http://%s:%d/%s"+ EndpointConsts.DEFAULT_CONTEXT+"/webRegister?_action=%s";
    public static final String ACTION_REG = "regist";
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_RESUME = "resume";

    public static class RegistState {
        public static int INIT = 1;//初始状态，出现在启动不自动注册的情况下
        public static int PAUSING = 2;//暂停
        public static int RUNNING = 3;//正常
    }
}
