package com.lianqu1990.common.bean.page;

import java.util.List;

public class Pager<T> implements Pageable {

    private static final long serialVersionUID = 8337463880134937842L;
    private static final int DEFAULT_PAGESIZE = 10;
    /**
     * 当前页数据
     */
    private List<T> data;
    /**
     * 当前页码
     */
    private int currentPage;
    /**
     * 第一条记录(this.currentPage - 1) * this.onePageSize
     */
    private int firstResult;
    /**
     * 总记录数
     */
    private long totalResults;
    /**
     * 每页条数
     */
    private int onePageSize;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 下一页页码
     */
    private int nextPage;
    /**
     * 上一页页码
     */
    private int previousPage;

    public Pager() {
        this(1,DEFAULT_PAGESIZE);
    }

    public Pager(int currentPage, int onePageSize) {
        if (currentPage > 1)
            this.currentPage = currentPage;
        else
            this.currentPage = 1;
        this.onePageSize = onePageSize;
        this.firstResult = (this.currentPage - 1) * this.onePageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        if (currentPage <= 0)
            this.currentPage = 1;
        if (totalPage != 0 && currentPage > this.totalPage)
            this.currentPage = totalPage;
        this.firstResult = (this.currentPage - 1) * this.onePageSize;

    }

    public int getOnePageSize() {
        return onePageSize;
    }

    public void setOnePageSize(int onePageSize) {
        this.onePageSize = onePageSize;
    }

    public long getTotalResults() {
        return totalResults;
    }

    /**
     * 设置总记录数，会自动计算出分页数据
     *
     * @param totalResults
     * @Title: setTotalResults
     * @Author: hp 2013-4-8 上午10:03:53
     */
    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
        if (totalResults % this.onePageSize == 0) {
            this.totalPage = (int) (totalResults / this.onePageSize);
        } else {
            this.totalPage = (int) Math.floor(totalResults / this.onePageSize) + 1;
        }

        if (this.totalPage == 0) {
            this.totalPage = 1;
        }
        if (this.currentPage > totalPage) {
            this.currentPage = totalPage;
        }
        if (this.currentPage > 1) {
            this.previousPage = this.currentPage - 1;
        } else {
            this.previousPage = 1;
        }
    }

    public int getFirstResult() {
        return firstResult;
    }

    public int getNextPage() {
        if (this.currentPage < this.totalPage) {
            this.nextPage = this.currentPage + 1;
        } else {
            this.nextPage = this.totalPage;
        }
        return nextPage;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public int getTotalPage() {
        if (totalResults < 0) {
            return -1;
        }
        return totalPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }


    /**
     * alias method
     */
    public List<T> getResutls() {
        return data;
    }

    public long getCursor() {
        return firstResult;
    }

    public int getTotal() {
        return (int) totalResults;
    }

}
