package com.elastic.search.elasticsearch.dataobject;


import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 按条件更新 请求参数
 *
 * @author niuzhiwei
 */
public class ConditionUpdateESObject extends UpdateESObject implements Serializable {


    private static final long serialVersionUID = 4730396395563341460L;
    /**
     * 筛选条件
     */
    private List<SearchCondition> conditions;

    public ConditionUpdateESObject() {
    }

    public ConditionUpdateESObject(String systemName, String indexName, String typeName) {
        super(systemName, indexName, typeName);
    }

    public List<SearchCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<SearchCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConditionUpdateESObject{");
        sb.append(super.toString());
        sb.append("conditions=").append(conditions);
        sb.append('}');
        return sb.toString();
    }
}

