package com.lianqu1990.common.utils.date;

import java.util.Date;

/**
 * @author hanchao
 * @date 2017/4/9 22:35
 */
public class TimestampUtil {
    public static int getUnixTimeStamp(Date date){
        return date == null ? 0 : (int) (date.getTime() / 1000);
    }
    /**
     * unix时间戳
     * @return 当前unix时间戳
     */
    public static int currentUnixTimeStamp(){
        return (int) (currentTimeStamp()/1000);
    }

    /**
     * 毫秒时间戳
     * @return 当前毫秒时间戳
     */
    public static long currentTimeStamp(){
        return System.currentTimeMillis();
    }

}
