package com.huatu.morphling.common.bean;

import com.huatu.morphling.utils.pattern.RegexConst;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Pattern;

/**
 * @author hanchao
 * @date 2017/11/8 10:43
 */
@Data
public class UserinfoFormDTO {
    @NotBlank
    private String name;
    @Pattern(regexp = RegexConst.PHONE_CHECK)
    private String phone;
    @Email
    private String email;
    @URL
    private String headImg;
}
