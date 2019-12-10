package com.elastic.search.elasticsearch.dataobject;


import com.elastic.search.elasticsearch.dataobject.conditions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 查询请求参数，内部使用链表实现分组查询
 *
 * @author niuzhiwei
 */
public class QueryESObject extends BaseESObject implements Serializable {
    private static final long serialVersionUID = 7016340994510736836L;

    /**
     * 版本兼容标志（检索条件变更） 历史版本： 1 （2017年9月01日之前） 2 （2017年11月14日之后）
     */
    private Integer version = 1;

    /**
     * 过滤条件，内部均使用 AND 链接
     */
    private List<SearchCondition> searchConditions;

    /**
     * 排序条件，依据给定的list顺序进行检索排序，只有链头参数有效
     */

    private List<OrderCondition> orderConditions;

    /**
     * 指定需要的属性，默认返回所有，只有链头参数有效
     */
    private List<String> needFields;

    /**
     * 分页条件，默认最多1000页，只有链头参数有效
     */
    private PageCondition pageCondition;

    /**
     * OR 链接查询参数,只需设置 searchConditions 参数即可
     * <p>
     * 替代 多条件使用 多层嵌套不便，使用 链表实现 {@link SearchCondition#nextAndCondition}
     */
    @Deprecated
    private QueryESObject nextGroupQuery;
    /**
     * 分组条件
     */
    private GroupByCondition groupByCondition;

    /**
     * 分组统计后，是否仍然需要返回查询到的文档内容
     * <p>
     * 若分组查询，该条件优于needFields字段
     * <p>
     * <code>false</code>不需要返回,<code>true</code>需要返回
     */
    private boolean groupAndNeedSource;

    public boolean groupCondition() {
        return nextGroupQuery != null;
    }

    public boolean filteField() {
        return needFields != null && needFields.size() > 0;
    }

    public QueryESObject() {
    }

    public QueryESObject(String systemName, String indexName, String typeName) {
        super(systemName, indexName, typeName);
    }

    public String[] getFunctionNames() {
        if (groupByCondition == null) {
            return null;
        }

        List<String> functionNames = new ArrayList<>();
        final List<FunctionCondition> functions = groupByCondition.getFunctionConditions();
        if (functions == null || functions.size() == 0) {
            return null;
        }
        for (FunctionCondition function : functions) {
            functionNames.add(function.getFunctionName());
        }
        return functionNames.toArray(new String[0]);
    }

    public boolean groupSearch() {
        final String[] functionNames = getFunctionNames();
        if (functionNames == null || functionNames.length == 0) {
            return false;
        }
        return true;
    }

    public boolean pageSearch() {
        return pageCondition != null;
    }

    public String getLastGroupCondition() {
        if (groupByCondition == null) {
            return null;
        }
        final List<String> groupFields = groupByCondition.getGroupFields();
        return groupFields.get(groupFields.size() - 1);
    }

    public List<SearchCondition> getSearchConditions() {
        return searchConditions;
    }

    public void setSearchConditions(List<SearchCondition> searchConditions) {
        this.searchConditions = searchConditions;
    }

    public List<OrderCondition> getOrderConditions() {
        return orderConditions;
    }

    public void setOrderConditions(List<OrderCondition> orderConditions) {
        this.orderConditions = orderConditions;
    }

    public List<String> getNeedFields() {
        return needFields;
    }

    public void setNeedFields(List<String> needFields) {
        this.needFields = needFields;
    }

    public PageCondition getPageCondition() {
        return pageCondition;
    }

    public void setPageCondition(PageCondition pageCondition) {
        this.pageCondition = pageCondition;
    }

    public QueryESObject getNextGroupQuery() {
        return nextGroupQuery;
    }

    public void setNextGroupQuery(QueryESObject nextGroupQuery) {
        this.nextGroupQuery = nextGroupQuery;
    }

    public GroupByCondition getGroupByCondition() {
        return groupByCondition;
    }

    public void setGroupByCondition(GroupByCondition groupByCondition) {
        this.groupByCondition = groupByCondition;
    }

    public boolean isGroupAndNeedSource() {
        return groupAndNeedSource;
    }

    public void setGroupAndNeedSource(boolean groupAndNeedSource) {
        this.groupAndNeedSource = groupAndNeedSource;
    }

    /**
     * 格式未进行约束，调用方请勿尝试解析该字符串
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QueryESObject{");
        sb.append(super.toString());
        sb.append("searchConditions=").append(searchConditions);
        sb.append(", orderConditions=").append(orderConditions);
        sb.append(", needFields=").append(needFields);
        sb.append(", pageCondition=").append(pageCondition);
        sb.append(", nextGroupQuery=").append(nextGroupQuery);
        sb.append(", groupByCondition=").append(groupByCondition);
        sb.append(", groupAndNeedSource=").append(groupAndNeedSource);
        sb.append('}');
        return sb.toString();
    }
}
