package com.lianqu1990.morphling.common.bean;

import lombok.Data;

/**
 * @author hanchao
 * @date 2017/12/6 10:13
 */
@Data
public class UserAppVO {
    private int id;//user-app表的id
    private String appName;
    private String appDes;
    private String env;
}
