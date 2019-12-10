package com.elastic.search.elasticsearch.condition;


import com.elastic.search.elasticsearch.dataobject.QueryESObject;
import com.elastic.search.elasticsearch.dataobject.conditions.OrderCondition;
import com.elastic.search.elasticsearch.dataobject.conditions.PageCondition;
import com.elastic.search.elasticsearch.dataobject.enums.SortEnum;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * <p>
 * es 5.5.2 对资源的过滤进行了整合，统一构建资源过滤查询
 *
 * @author niuzhiwei
 */
public class SearchSourceBuilder {

    @Value(value = "${escenter.page.max.size:100}")
    private int pageMaxSize;

    public void build(final SearchRequestBuilder searchRequestBuilder, final QueryESObject esObject) {

        final org.elasticsearch.search.builder.SearchSourceBuilder searchSourceBuilder = org.elasticsearch.search.builder.SearchSourceBuilder.searchSource();
        // 构建分页资源查询
        page(esObject.getPageCondition(), searchSourceBuilder);
        // 构建 域 过滤器
        field(esObject.getNeedFields(), searchSourceBuilder);
        // 构建 排序条件
        sort(esObject.getOrderConditions(), searchSourceBuilder);

        searchRequestBuilder.setSource(searchSourceBuilder);
    }

    /**
     * 页 查询
     *
     * @param pageCondition
     * @return
     */
    public void page(PageCondition pageCondition, final org.elasticsearch.search.builder.SearchSourceBuilder searchSourceBuilder) {
        final PageCondition page = getPage(pageCondition);
        if (page != null) {
            searchSourceBuilder.from(page.getPageSize() * (page.getCurrentPage() - 1));
            searchSourceBuilder.size(page.getPageSize());
        }
    }

    public PageCondition getPage(PageCondition pageCondition) {
        if (null == pageCondition) {
            pageCondition = new PageCondition();
            pageCondition.setPageSize(1000);
            pageCondition.setCurrentPage(1);
            return pageCondition;
        }

        Integer currentPage = pageCondition.getCurrentPage();
        currentPage = currentPage == null ? 1 : currentPage < 1 ? 1 : currentPage;

        Integer pageSize = pageCondition.getPageSize();
        pageSize = pageSize == null ? 1 : pageSize > pageMaxSize ? pageMaxSize : pageSize < 1 ? 1 : pageSize;

        pageCondition.setCurrentPage(currentPage);
        pageCondition.setPageSize(pageSize);

        return pageCondition;
    }

    /**
     * 域 过滤
     *
     * @param needFields          需要过滤的域
     * @param searchSourceBuilder 查询请求构建器
     */
    public void field(final List<String> needFields, org.elasticsearch.search.builder.SearchSourceBuilder searchSourceBuilder) {
        if (needFields == null || needFields.size() == 0) {
            searchSourceBuilder.fetchSource(Boolean.TRUE);
            return;
        }

        searchSourceBuilder.fetchSource(needFields.toArray(new String[0]), null);
    }

    /**
     * 排序条件
     *
     * @param orderConditions     排序条件
     * @param searchSourceBuilder 查询请求构建器
     */
    public void sort(final List<OrderCondition> orderConditions, org.elasticsearch.search.builder.SearchSourceBuilder searchSourceBuilder) {
        if (null == orderConditions || orderConditions.isEmpty()) {
            return;
        }
        for (OrderCondition orderCondition : orderConditions) {
            searchSourceBuilder.sort(getSort(orderCondition));
        }
    }

    public FieldSortBuilder getSort(OrderCondition orderCondition) {
        final String fieldName = orderCondition.getFieldName();
        final SortEnum sort = orderCondition.getOrderCondition();

        FieldSortBuilder order;
        switch (sort) {
            case ASC:
                order = SortBuilders.fieldSort(fieldName).order(SortOrder.ASC);
                break;
            case DESC:
                order = SortBuilders.fieldSort(fieldName).order(SortOrder.DESC);
                break;
            default:
                order = null;
        }
        return order;
    }
}

