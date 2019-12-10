package com.elastic.search.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * @param <T>
 * @author niuzhiwei
 */
public class PagedList<T> implements Serializable {


    private static final long serialVersionUID = 8160823395759291587L;

    /**
     * 数据项
     */
    private List<T> items;
    /**
     * 总数量
     */
    private int totalCount;
    /**
     * 当前页码
     */
    private int currentPage;
    /**
     * 每页条数
     */
    private int pageSize;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public static <T> PagedList<T> createInstance(int pageNo, int pageSize, int totalCount, List<T> items) {
        PagedList<T> pagedList = new PagedList<T>();
        pagedList.setCurrentPage(pageNo);
        pagedList.setPageSize(pageSize);
        pagedList.setTotalCount(totalCount);
        pagedList.setItems(items);
        return pagedList;
    }

    public static <T> PagedList<T> createInstance(int pageNo, int pageSize, int totalCount, Class<T> t) {
        PagedList<T> pagedList = new PagedList<T>();
        pagedList.setCurrentPage(pageNo);
        pagedList.setPageSize(pageSize);
        pagedList.setTotalCount(totalCount);
        return pagedList;
    }

    @JsonIgnore
    public int getFirstIndex() {
        return (this.getCurrentPage() - 1) * pageSize;
    }

    @JsonIgnore
    public boolean isValidPageNo() {
        long totalPage = 0;
        if (totalCount % pageSize == 0) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }
        if (getCurrentPage() < 1 || getCurrentPage() > totalPage) {
            return false;
        }
        return true;
    }
}

