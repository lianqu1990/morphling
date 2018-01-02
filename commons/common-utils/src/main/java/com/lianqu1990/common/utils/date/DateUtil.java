package com.lianqu1990.common.utils.date;

import java.util.Calendar;
import java.util.Date;

/**
 * @author hanchao
 * @date 2017/4/27 13:37
 */
public class DateUtil {


    /**
     * 当前增减天
     * @param days
     * @return
     */
    public static Date addDay(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 某天增减天
     * @param date
     * @param days
     * @return
     */
    public static Date addDay(Date date,int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }


    /**
     * 获取当天最早时间
     * @return
     */
    public static long getBeginOfCurrentDay(){
        return getBegin(Calendar.HOUR_OF_DAY,new Date()).getTimeInMillis();
    }
    public static Date getBeginDateOfCurrentDay(){
        return getBegin(Calendar.HOUR_OF_DAY,new Date()).getTime();
    }
    public static long getBeginOfDay(Date date){
        return getBegin(Calendar.HOUR_OF_DAY,date).getTimeInMillis();
    }
    public static Date getBeginDateOfDay(Date date){
        return getBegin(Calendar.HOUR_OF_DAY,date).getTime();
    }

    /**
     * 获取当天最晚时间
     */
    public static long getEndOfCurrentDay(){
        return getEnd(Calendar.HOUR_OF_DAY,new Date()).getTimeInMillis();
    }
    public static Date getEndDateOfCurrentDay(){
        return getEnd(Calendar.HOUR_OF_DAY,new Date()).getTime();
    }
    public static long getEndOfDay(Date date){
        return getEnd(Calendar.HOUR_OF_DAY,date).getTimeInMillis();
    }
    public static Date getEndDateOfDay(Date date){
        return getEnd(Calendar.HOUR_OF_DAY,date).getTime();
    }


    /**
     * 获取当天的第二天的最早时间，更精确的当天最晚时间，方便用小于
     * @return
     */
    public static long getBeginOfNextDay(){
        return getEnd(Calendar.HOUR_OF_DAY,addDay(1)).getTimeInMillis();
    }
    public static Date getBeginDateOfNextDay(){
        return getBegin(Calendar.HOUR_OF_DAY,addDay(1)).getTime();
    }
    public static long getBeginOfNextDay(Date date){
        return getBegin(Calendar.HOUR_OF_DAY,addDay(date,1)).getTimeInMillis();
    }
    public static Date getBeginDateOfNextDay(Date date){
        return getBegin(Calendar.HOUR_OF_DAY,addDay(date,1)).getTime();
    }


    /**
     * 获取某个单位下的起始时间，最大到年
     * 周一是周内第一天
     * @param unit
     * @param date
     * @return
     */
    public static Calendar getBegin(int unit,Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        do{
            if(Calendar.MILLISECOND < unit) break;
            calendar.set(Calendar.MILLISECOND,0);
            if(Calendar.SECOND < unit) break;
            calendar.set(Calendar.SECOND,0);
            if(Calendar.MINUTE < unit) break;
            calendar.set(Calendar.MINUTE,0);
            if(Calendar.HOUR_OF_DAY < unit) break;
            calendar.set(Calendar.HOUR_OF_DAY,0);
            if(Calendar.DAY_OF_WEEK < unit) break;
            if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                calendar.add(Calendar.DAY_OF_MONTH,-7);
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            }else{
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            }
            if(calendar.DAY_OF_MONTH < unit) break;
            calendar.set(Calendar.DAY_OF_MONTH,1);
            if(calendar.MONTH < unit) break;
            calendar.set(calendar.MONTH,Calendar.JANUARY);
        } while(false);
        return calendar;
    }


    /**
     * 获取某个单位下的起始时间，最大到年
     * 周一是周内第一天
     * @param unit
     * @param date
     * @return
     */
    public static Calendar getEnd(int unit,Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        do{
            if(Calendar.MILLISECOND < unit) break;
            calendar.set(Calendar.MILLISECOND,999);
            if(Calendar.SECOND < unit) break;
            calendar.set(Calendar.SECOND,59);
            if(Calendar.MINUTE < unit) break;
            calendar.set(Calendar.MINUTE,59);
            if(Calendar.HOUR_OF_DAY < unit) break;
            calendar.set(Calendar.HOUR_OF_DAY,23);
            if(Calendar.DAY_OF_WEEK < unit) break;
            if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
            }else{
                calendar.add(Calendar.DAY_OF_MONTH,7);
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
            }
            if(calendar.DAY_OF_MONTH < unit) break;
            calendar.set(Calendar.DAY_OF_MONTH,0);
            if(calendar.MONTH < unit) break;
            calendar.set(calendar.MONTH,Calendar.UNDECIMBER);
        } while(false);
        return calendar;
    }

}
