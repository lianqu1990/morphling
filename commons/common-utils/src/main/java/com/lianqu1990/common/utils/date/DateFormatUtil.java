package com.lianqu1990.common.utils.date;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @author hanchao
 * @date 2017/2/12 16:43
 */
public class DateFormatUtil {
    public static final FastDateFormat DEFAULT_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public static final FastDateFormat NORMAL_DAY_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
    public static final FastDateFormat NORMAL_TIME_FORMAT = FastDateFormat.getInstance("HH:mm:ss");

    public static final FastDateFormat LONG_NUMBER_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");
    public static final FastDateFormat NUMBER_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss");


}
