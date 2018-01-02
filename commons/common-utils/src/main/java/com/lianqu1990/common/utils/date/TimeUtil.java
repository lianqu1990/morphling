package com.lianqu1990.common.utils.date;

import java.util.Date;

/**
 * @author hanchao
 * @date 2017/4/29 13:48
 */
public class TimeUtil {
    public static final long MILLS_SECOND = 1000L;
    public static final int SECONDS_MINUTE = 60;
    public static final int SECONDS_HOUR = SECONDS_MINUTE*60;
    public static final int SECONDS_DAY = SECONDS_HOUR*24;

    /**
     * 比较两个时间的毫秒差
     * @param date1
     * @param date2
     * @return
     */
    public static long timeDifference(Date date1, Date date2) {
        if (date1 == null || date2 == null) throw new IllegalArgumentException("Expecting date parameter not to be null");
        return Math.abs(date1.getTime() - date2.getTime());
    }

}
