package com.lianqu1990.morphling.common.bean.degrade;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author hanchao
 * @date 2017/12/21 14:19
 */
@Data
public class NamespaceConfig {

    private String appId;
    private String clusterName;
    private String namespaceName;
    private String name;
    private String comment;
    private String dataChangeCreatedBy;
    private String dataChangeLastModifiedBy;
    private String dataChangeCreatedTime;
    private String dataChangeLastModifiedTime;
    private List<ConfItem> items;

    /**
     * 请求release版本才有此属性
     */
    private Map<String,String> configurations;


    /**
     * 请求非release版本才有此属性
     */



    @Data
    public static class ConfItem {
        private String key;
        private String value;
        private String comment;
        private String dataChangeCreatedBy;
        private String dataChangeLastModifiedBy;
        private String dataChangeCreatedTime;
        private String dataChangeLastModifiedTime;

    }

}
