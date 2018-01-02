package com.lianqu1990.common;


import java.io.Serializable;

/**
 * success-error可以不做区分，可以再抽取immultable,set方法全部用来生成新实例(这种响应只针对目前的error类别，也就多一个定义)
 * Response 返回结果抽象类
 * Created by shaojieyue
 * Created time 2016-04-18 11:32
 */
public interface Result extends Serializable {
    int SUCCESS_CODE=1000000;
    /**
     * 获取结果code
     * @return
     */
    public int getCode();
}
