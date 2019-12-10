package com.elastic.search.elasticsearch.dataobject.enums;

import lombok.Getter;

/**
 * <p>
 * es 查询条件表达式
 *
 * @author niuzhiwei
 */
@Getter
public enum ConditionExpressionEnum {
    /**
     * 查询条件表达式枚举
     */
    EQUAL("等于"),
    UNEQUAL("不等"),
    GREATER("大于"),
    GREATER_OR_EQUAL("大于等于"),
    LESSER("小于"),
    LESSER_OR_EQUAL("小于等于"),
    LIKE("模糊查询"),
    NULL("空值"),
    NOT_NULL("非空"),
    IN("在"),
    NOT_IN("不在"),
    BETWEEN("不包含边界值"),
    BETWEEN_AND("包含边界值"),
    BETWEEN_LEFT("含左不含右"),
    BETWEEN_RIGHR("含右不含左"),
    MATCH("匹配查询，主要用于分词字段的全文检索");

    /**
     * 描述
     */

    private String desc;

    ConditionExpressionEnum(String desc) {
        this.desc = desc;
    }

}
