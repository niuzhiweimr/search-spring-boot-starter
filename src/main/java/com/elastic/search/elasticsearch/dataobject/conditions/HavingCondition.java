package com.elastic.search.elasticsearch.dataobject.conditions;


import com.elastic.search.elasticsearch.dataobject.enums.ConditionExpressionEnum;

import java.io.Serializable;

/**
 * <p>
 * SQL 分组筛选条件
 *
 * @author niuzhiwei
 */
public class HavingCondition implements Serializable {

    private static final long serialVersionUID = -8856231949172866442L;
    /**
     * 字段
     */
    private String filedName;
    /**
     * 筛选表达式
     */
    private ConditionExpressionEnum conditionExpressionEnum;
    /**
     * 字段值
     */
    private Object fieldValue;

    public HavingCondition() {
    }

    public HavingCondition(String filedName, ConditionExpressionEnum conditionExpressionEnum, Object fieldValue) {
        this.filedName = filedName;
        this.conditionExpressionEnum = conditionExpressionEnum;
        this.fieldValue = fieldValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HavingCondition{");
        sb.append("filedName='").append(filedName).append('\'');
        sb.append(", conditionExpressionEnum=").append(conditionExpressionEnum);
        sb.append(", fieldValue=").append(fieldValue);
        sb.append('}');
        return sb.toString();
    }
}
