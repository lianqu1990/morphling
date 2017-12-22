package com.huatu.morphling.common.bean.degrade;

import com.huatu.morphling.common.consts.DegradeConsts;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hanchao
 * @date 2017/12/21 17:58
 */
@Data
@AllArgsConstructor
public class ReleaseApplyDTO {
    private String releaseTitle;
    private String releaseComment;
    private final String releasedBy= DegradeConsts.UPDATE_USER;
}
