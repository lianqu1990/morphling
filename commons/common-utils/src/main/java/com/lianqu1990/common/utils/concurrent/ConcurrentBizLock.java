package com.lianqu1990.common.utils.concurrent;

import com.lianqu1990.common.utils.function.Task;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 利用jucmap实现穿透限流
 * @author hanchao
 * @date 2017/10/18 11:22
 */
public class ConcurrentBizLock {
    private static ConcurrentHashMap<Object,Object> locks = new ConcurrentHashMap();
    private static final boolean V = true;

    public static boolean tryLock(Object key){
        Object result = locks.putIfAbsent(key, V);
        return result == null ? true : false;
    }

    public static boolean releaseLock(Object key){
        Object result = locks.remove(key);
        return result == null ? true : false;
    }

    public static boolean atomicTask(Object key, Recheck recheck, Task task){
        if(tryLock(key)){
            try {
                if(recheck != null && recheck.check()){
                    task.execute();
                    return true;
                }
            } catch(Exception e){
                e.printStackTrace();
            } finally {
                releaseLock(key);
            }
        }
        return false;
    }


    public interface Recheck{
        boolean check() throws InterruptedException;
    }

}
