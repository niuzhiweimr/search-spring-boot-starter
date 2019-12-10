package com.elastic.search.elasticsearch.infrastructure.utils;


import com.elastic.search.elasticsearch.dataobject.conditions.PageCondition;

/**
 * <p>
 * 分页工具类
 *
 * @author niuzhiwei
 */
public class PageConditionUtils {

    private PageConditionUtils() {

    }

    /**
     * 创建分页条件
     *
     * @param pageSize
     * @param currentPage
     * @return
     */
    public static PageCondition create(int pageSize, int currentPage) {
        PageCondition pageCondition = new PageCondition();
        pageCondition.setCurrentPage(currentPage);
        pageCondition.setPageSize(pageSize);
        return pageCondition;
    }
}
