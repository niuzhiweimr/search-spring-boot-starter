package com.elastic.search.elasticsearch.dataobject.conditions;

import java.io.Serializable;

/**
 * <p>
 * es 分页条件
 *
 * @author niuzhiwei
 */
public class PageCondition implements Serializable {


    private static final long serialVersionUID = -1740674755812239502L;
    /**
     * 页大小
     */
    private Integer pageSize;
    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     * 总文档数
     */
    private Long totalDocs;

    public PageCondition() {
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Long getTotalDocs() {
        return totalDocs;
    }

    public void setTotalDocs(Long totalDocs) {
        this.totalDocs = totalDocs;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PageCondition{");
        sb.append("pageSize=").append(pageSize);
        sb.append(", currentPage=").append(currentPage);
        sb.append(", totalDocs=").append(totalDocs);
        sb.append('}');
        return sb.toString();
    }
}
