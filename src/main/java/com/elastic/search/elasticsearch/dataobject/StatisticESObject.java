package com.elastic.search.elasticsearch.dataobject;


import com.elastic.search.elasticsearch.dataobject.conditions.FunctionCondition;
import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 统计请求参数
 *
 * @author niuzhiwei
 */
public class StatisticESObject extends BaseESObject implements Serializable {

    private static final long serialVersionUID = -6208691852851782250L;
    /**
     * 过滤条件
     */
    private List<SearchCondition> searchConditions;

    /**
     * 统计函数
     */
    private List<FunctionCondition> functionConditions;

    public StatisticESObject() {
    }

    public StatisticESObject(String systemName, String indexName, String typeName) {
        super(systemName, indexName, typeName);
    }

    public List<SearchCondition> getSearchConditions() {
        return searchConditions;
    }

    public void setSearchConditions(List<SearchCondition> searchConditions) {
        this.searchConditions = searchConditions;
    }

    public List<FunctionCondition> getFunctionConditions() {
        return functionConditions;
    }

    public void setFunctionConditions(List<FunctionCondition> functionConditions) {
        this.functionConditions = functionConditions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatisticESObject{");
        sb.append(super.toString());
        sb.append("searchConditions=").append(searchConditions);
        sb.append(", functionConditions=").append(functionConditions);
        sb.append('}');
        return sb.toString();
    }
}
