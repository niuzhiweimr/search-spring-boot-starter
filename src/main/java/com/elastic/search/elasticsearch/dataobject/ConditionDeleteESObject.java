package com.elastic.search.elasticsearch.dataobject;


import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 按条件删除 请求参数
 *
 * @author niuzhiwei
 */
public class ConditionDeleteESObject extends DeleteESObject implements Serializable {


    private static final long serialVersionUID = 5343879260893305208L;
    /**
     * 筛选条件
     */
    private List<SearchCondition> conditions;

    public ConditionDeleteESObject() {
    }

    public ConditionDeleteESObject(String systemName, String indexName, String typeName) {
        super(systemName, indexName, typeName);
    }

    public ConditionDeleteESObject(List<SearchCondition> conditions) {
        this.conditions = conditions;
    }

    public List<SearchCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<SearchCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConditionDeleteESObject{");
        sb.append(super.toString());
        sb.append("conditions=").append(conditions);
        sb.append('}');
        return sb.toString();
    }
}
