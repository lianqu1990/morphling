package com.huatu.morphling.common.bean.degrade;

import lombok.Data;

import java.util.List;

/**
 * @author hanchao
 * @date 2017/12/21 14:49
 */
@Data
public class DegradeEndpoint {
    /**
     * degradeInfos : [{"key":"userBuy","bizName":"用户已购课程"},{"key":"courseLimit","bizName":"课程已购数量"},{"key":"courseDetailV3","bizName":"课程详情v3"},{"key":"courseListV3","bizName":"课程列表v3"},{"key":"courseHtml","bizName":"课程详情HTML"},{"key":"collectionCourse","bizName":"课程合集"}]
     * appId : tiku-course-server
     * env : test
     */

    private String appId;
    private String env;
    private List<DegradeItem> degradeInfos;


    @Data
    public static class DegradeItem {
        /**
         * key : userBuy
         * bizName : 用户已购课程
         */

        private String key;
        private String bizName;
        private boolean open;
    }
}
