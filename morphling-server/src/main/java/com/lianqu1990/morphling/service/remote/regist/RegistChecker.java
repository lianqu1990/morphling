package com.lianqu1990.morphling.service.remote.regist;

import com.lianqu1990.morphling.dao.jpa.entity.Env;

/**
 * @author hanchao
 * @date 2017/12/6 16:57
 */
public interface RegistChecker {
    /**
     * 是否可以处理该服务
     * @param type
     * @return
     */
    boolean resolve(int type);

    /**
     * 检测是否正常
     * @param serviceName
     * @param host
     * @param port
     * @return
     */
    boolean health(String serviceName, String host, int port, Env.EnvProperties envProperties);
}
