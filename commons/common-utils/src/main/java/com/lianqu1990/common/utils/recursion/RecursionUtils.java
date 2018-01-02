package com.lianqu1990.common.utils.recursion;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;

/**
 * @author hanchao
 * @date 2017/10/6 11:15
 */
public class RecursionUtils {
    /**
     * 递归将数据放入collection
     * @param func
     * @param <T>
     * @return
     */
    public static <T> void collect(T t,CollectionFunc<T> func){
        Collection<T> recursion = func.recursion(t);
        if(CollectionUtils.isNotEmpty(recursion)){
            for (T tmp : recursion) {
                collect(tmp,func);
            }
        }
    }

    /**
     * 递归处理数据,返回最后节点
     * @param t
     * @param func
     * @param <T>
     */
    public static <T> T deal(T t,ObjectFunc<T> func){
        T recursion = func.recursion(t);
        if(recursion != null){
            return deal(recursion,func);
        }else{
            return t;
        }
    }


    /**
     * 递归的出口是返回了空的集合
     * @param <T>
     */
    public interface CollectionFunc<T>{
        Collection<T> recursion(T t);
    }

    /**
     * 以对象为出口,为null则退出
     * @param <T>
     */
    public interface ObjectFunc<T>{
        T recursion(T t);
    }

}
