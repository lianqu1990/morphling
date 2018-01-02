package com.lianqu1990.morphling.common.bean.degrade;

import com.lianqu1990.morphling.common.consts.DegradeConsts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hanchao
 * @date 2017/12/21 17:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequesetDTO {
    private String key;
    private String value;
    private String comment;
    private final String dataChangeLastModifiedBy = DegradeConsts.UPDATE_USER;
    private final String dataChangeCreatedBy = DegradeConsts.UPDATE_USER;

}
