package com.lianqu1990.morphling.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hanchao
 * @date 2017/11/20 14:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecResult {
    public static final String SUCCESS = "0";
    public static final String ERROR = "1";


    private String status;
    private String result;
}
