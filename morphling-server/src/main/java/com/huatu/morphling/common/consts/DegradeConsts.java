package com.huatu.morphling.common.consts;

/**
 * @author hanchao
 * @date 2017/12/21 13:30
 */
public class DegradeConsts {
    public static final String HEADER_AUTH = "Authorization";
    public static final String TOKEN = "cb61f2e454f49b21ad30d45e303a5f0cca6bad07";

    public static final String OPEN = "1";
    public static final String CLOSED = "0";

    public static final String NAMESPACE_DEGRADE = "degrade";

    public static final String CLUSTER_DEFAULT = "default";

    public static final String CONF_GET_RELEASE_URL = "http://%s/openapi/v1/envs/{env}/apps/{appId}/clusters/{clusterName}/namespaces/{namespaceName}/releases/latest";

    public static final String CONF_GET_SNAPSHOT_URL = "http://%s/openapi/v1/envs/{env}/apps/{appId}/clusters/{clusterName}/namespaces/{namespaceName}";

    public static final String CONF_ADD_URL = "http://%s/openapi/v1/envs/{env}/apps/{appId}/clusters/{clusterName}/namespaces/{namespaceName}/items";
    public static final String CONF_UPDATE_URL = "http://%s/openapi/v1/envs/{env}/apps/{appId}/clusters/{clusterName}/namespaces/{namespaceName}/items/{key}";

    public static final String CONF_APPLY_URL = "http://%s/openapi/v1/envs/{env}/apps/{appId}/clusters/{clusterName}/namespaces/{namespaceName}/releases";

    /**
     *
     */
    public static final String UPDATE_USER = "apollo";
}
