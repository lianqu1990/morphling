package com.lianqu1990.morphling.common.bean;

import lombok.Data;

/**
 * @author hanchao
 * @date 2017/12/5 13:18
 */
@Data
public class AppInstanceVO {
    private int id;
    private String host;
    private int port;
    private int clientPort;
    private String currentVersion;
    private String clientName;
    private String appName;
    private String contextPath;
    private byte status;

    private boolean registStatus;
}
