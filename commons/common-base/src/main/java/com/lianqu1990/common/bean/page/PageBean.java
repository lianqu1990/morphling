package com.lianqu1990.common.bean.page;

import java.util.List;

/**
 * 分页bean通用
 * Created by shaojieyue
 * Created time 2016-06-15 16:34
 */

public class PageBean<T> implements Pageable{
    private List<T> resutls;//结果集
    private long cursor;//游标
    private int total;//总记录数

    public PageBean(List<T> resutls, long cursor, int total) {
        this.resutls = resutls;
        this.cursor = cursor;
        this.total = total;
    }

    public PageBean() {
    }

    public List<T> getResutls() {
        return resutls;
    }

    public void setResutls(List<T> resutls) {
        this.resutls = resutls;
    }

    public long getCursor() {
        return cursor;
    }

    public void setCursor(long cursor) {
        this.cursor = cursor;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
