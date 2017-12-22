package com.huatu.morphling.common.bean;

import lombok.Data;

/**
 * @author hanchao
 * @date 2017/12/15 13:13
 */
@Data
public class GitTag {
    private String tagName;
    private String remark;
    private String module;
    private String branch;
    private String createTime;
    private String createBy;
}
