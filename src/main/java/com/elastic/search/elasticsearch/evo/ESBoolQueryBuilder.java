package com.elastic.search.elasticsearch.evo;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * @author niuzhiwei
 */
public class ESBoolQueryBuilder {

    private BoolQueryBuilder boolQueryBuilder;

    public ESBoolQueryBuilder(BoolQueryBuilder boolQueryBuilder) {
        this.boolQueryBuilder = boolQueryBuilder;
    }

    public void must(QueryBuilder builder) {
        this.boolQueryBuilder.must(builder);
    }

    public void should(QueryBuilder builder) {
        this.boolQueryBuilder.should(builder);
    }

    public void mustNot(QueryBuilder builder) {
        this.boolQueryBuilder.mustNot(builder);
    }

    public BoolQueryBuilder getBoolQueryBuilder() {
        return boolQueryBuilder;
    }
}
