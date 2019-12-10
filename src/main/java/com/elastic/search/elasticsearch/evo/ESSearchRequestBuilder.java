package com.elastic.search.elasticsearch.evo;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class ESSearchRequestBuilder {

    private SearchRequestBuilder searchRequestBuilder;
    private ESBoolQueryBuilder rootESQueryBuilder;

    public ESSearchRequestBuilder(SearchRequestBuilder searchRequestBuilder) {
        this.searchRequestBuilder = searchRequestBuilder;
    }

    public ESSearchRequestBuilder match(String name, Object value) {
        QueryBuilder builder = QueryBuilders.matchQuery(name, value);
        getRootESQueryBuilder().must(builder);
        return this;
    }

    public ESSearchRequestBuilder lt(String name, Object value) {
        QueryBuilder builder = QueryBuilders.rangeQuery(name).lt(value);
        getRootESQueryBuilder().must(builder);
        return this;
    }

    public ESSearchRequestBuilder and(ESQueryBuilder... builders) {
        ESBoolQueryBuilder rootESQueryBuilder = getRootESQueryBuilder();
        for (ESQueryBuilder builder : builders) {
            rootESQueryBuilder.must(builder.getQueryBuilder());
        }
        System.out.println(rootESQueryBuilder.getBoolQueryBuilder());
        return this;
    }

    public ESSearchRequestBuilder or(ESQueryBuilder... builders) {
        ESBoolQueryBuilder rootESQueryBuilder = getRootESQueryBuilder();
        for (ESQueryBuilder builder : builders) {
            rootESQueryBuilder.should(builder.getQueryBuilder());
        }
        System.out.println(rootESQueryBuilder.getBoolQueryBuilder());
        return this;
    }

    public ESSearchRequestBuilder not(ESQueryBuilder... builders) {
        ESBoolQueryBuilder rootESQueryBuilder = getRootESQueryBuilder();
        for (ESQueryBuilder builder : builders) {
            rootESQueryBuilder.mustNot(builder.getQueryBuilder());
        }
        System.out.println(rootESQueryBuilder.getBoolQueryBuilder());
        return this;
    }

//    public ESSearchBuilder and(String name, ESQueryType type, Object text) {
//        QueryBuilder builder = type.getQueryBuilder(name, text);
//        getBoolQueryBuilder().must(builder);
//        return this;
//    }
//
//    public ESSearchBuilder or(String name, ESQueryType type, Object text) {
//        QueryBuilder builder = type.getQueryBuilder(name, text);
//        System.out.println("\n\n\n"+getBoolQueryBuilder().should(builder));
//        return this;
//    }

    public SearchResponse get() {
        return searchRequestBuilder.get();
    }

    private ESBoolQueryBuilder getRootESQueryBuilder() {
        if (rootESQueryBuilder == null) {
            rootESQueryBuilder = new ESBoolQueryBuilder(QueryBuilders.boolQuery());
            searchRequestBuilder.setQuery(rootESQueryBuilder.getBoolQueryBuilder());
        }
        return rootESQueryBuilder;
    }

//    private ESSearchRequestBuilder must(QueryBuilder builder) {
//        getRootESQueryBuilder().must(builder);
//        return this;
//    }
}
