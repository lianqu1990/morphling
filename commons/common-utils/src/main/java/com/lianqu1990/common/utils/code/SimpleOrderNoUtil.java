package com.lianqu1990.common.utils.code;


import com.lianqu1990.common.utils.date.DateFormatUtil;

import java.util.Date;
import java.util.Random;

/**
 * @author hanchao
 * @date 2017/2/12 16:01
 */
public class SimpleOrderNoUtil {

    public static String createOrderNo(){
        int rand = new Random().nextInt(100);
        return DateFormatUtil.LONG_NUMBER_FORMAT.format(new Date())+String.format("%03d",rand);
    }

}
