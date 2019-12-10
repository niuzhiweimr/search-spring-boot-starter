package com.elastic.search.elasticsearch.dataobject.conditions;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * SQL 分组条件
 *
 * @author niuzhiwei
 */
public class GroupByCondition implements Serializable {


    private static final long serialVersionUID = -9062303883253151334L;
    /**
     * 分组字段
     */
    private List<String> groupFields;

    /**
     * 统计函数
     */
    private List<FunctionCondition> functionConditions;

    /**
     * having 筛选
     * <p>
     * TODO 目前考察es的过滤API，未提供对聚合结果的过滤
     * 1、继续学习，自定义桶过滤器；
     * 2、在封装结果层面进行简单过滤；
     */
    //private List<HavingCondition> havingConditions;
    public GroupByCondition() {
    }

    public List<String> getGroupFields() {
        return groupFields;
    }

    public void setGroupFields(List<String> groupFields) {
        this.groupFields = groupFields;
    }

    public List<FunctionCondition> getFunctionConditions() {
        return functionConditions;
    }

    public void setFunctionConditions(List<FunctionCondition> functionConditions) {
        this.functionConditions = functionConditions;
    }

	/*public List<HavingCondition> getHavingConditions() {
		return havingConditions;
	}

	public void setHavingConditions(List<HavingCondition> havingConditions) {
		this.havingConditions = havingConditions;
	}*/

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GroupByCondition{");
        sb.append("groupFields=").append(groupFields);
        sb.append(", functionConditions=").append(functionConditions);
        //sb.append(", havingConditions=").append(havingConditions);
        sb.append('}');
        return sb.toString();
    }
}
