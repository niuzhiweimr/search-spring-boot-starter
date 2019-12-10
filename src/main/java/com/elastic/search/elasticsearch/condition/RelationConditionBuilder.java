package com.elastic.search.elasticsearch.condition;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 检索条件关系 构建器
 * @author jack
 */
public class RelationConditionBuilder {

    /**
     * 构建 根查询 与 当前查询 的嵌套关系
     * <p>
     * 若有条件分组查询（OR 链接），那么所有关系均为 should ；否则为must
     *
     * @param groupCondition 是否条件分组
     * @param currentQuery   当前查询
     * @param rootQuery      根查询
     * @return 根查询
     */
    public QueryBuilder builder(boolean groupCondition, QueryBuilder currentQuery, BoolQueryBuilder rootQuery) {
        return groupCondition ? rootQuery.should(currentQuery) : rootQuery.must(currentQuery);
    }
}