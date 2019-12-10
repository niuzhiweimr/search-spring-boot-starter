package com.elastic.search.elasticsearch.service;


import com.elastic.search.elasticsearch.condition.CollapseConditionBuilder;
import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.elasticsearch.condition.GroupConditionBuilder;
import com.elastic.search.elasticsearch.condition.QueryConditionBuilder;
import com.elastic.search.elasticsearch.condition.SearchSourceBuilder;
import com.elastic.search.elasticsearch.dataobject.CollapseQueryObject;
import com.elastic.search.elasticsearch.dataobject.ESResponse;
import com.elastic.search.elasticsearch.dataobject.StatisticESObject;
import com.elastic.search.elasticsearch.dataobject.conditions.FunctionCondition;
import com.elastic.search.elasticsearch.infrastructure.handler.CollapseResponseHandler;
import com.elastic.search.elasticsearch.infrastructure.handler.ESQueryResponseHandler;
import com.elastic.search.elasticsearch.infrastructure.handler.ESStatisticResponseHandler;
import com.elastic.search.elasticsearch.search.log.SearchLogger;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author niuzhiwei
 */
public class ESStatisticOperatorService {

    @Resource
    private QueryConditionBuilder queryConditionBuilder;

    @Resource
    private GroupConditionBuilder groupConditionBuilder;

    @Resource
    private ESStatisticResponseHandler esStatisticResponseHandler;

    @Resource
    private SearchSourceBuilder searchSourceBuilder;

    @Resource
    private CollapseConditionBuilder collapseConditionBuilder;

    @Resource
    private CollapseResponseHandler collapseResponseHandler;

    @Resource
    private ESQueryResponseHandler esQueryResponseHandler;

    @Resource
    private TransportClient client;

    public SearchBaseResult<Map<String, Number>> statisticByConditions(StatisticESObject esObject) {
        SearchBaseResult<Map<String, Number>> SearchResult = new SearchBaseResult<>();

        final SearchRequestBuilder searchRequestBuilder = client.prepareSearch(esObject.getIndexName());
        // 构建查询条件
        searchRequestBuilder.setQuery(queryConditionBuilder.innerBuilder(esObject.getSearchConditions(), 1,
                esObject.getIndexName(), esObject.getTypeName()));

        // 设置聚合条件
        for (FunctionCondition functionCondition : esObject.getFunctionConditions()) {
            final AbstractAggregationBuilder aggregationFunction = groupConditionBuilder
                    .getAggregationFunction(functionCondition);
            searchRequestBuilder.addAggregation(aggregationFunction);
        }

        SearchResponse searchResponse;
        try {
            SearchLogger.log(searchRequestBuilder);
            searchResponse = searchRequestBuilder.execute().get();
            SearchLogger.log(searchResponse);
        } catch (Exception ex) {
            SearchLogger.error("statisticByConditions", ex);
            return SearchBaseResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "elastic error:" + ex.getMessage());
        }

        try {
            final Map<String, Number> statisticResult = esStatisticResponseHandler.handler(searchResponse.getAggregations());
            SearchResult.setResult(statisticResult);
            return SearchResult;
        } catch (Exception ex) {
            SearchLogger.error("escenter statistic error", ex);
            return SearchBaseResult.faild(ESErrorCode.ESCENTER_ERROR_CODE, "escenter error:" + ex.getMessage());
        }
    }

    public SearchBaseResult<Map<String, ESResponse>> collapse(final CollapseQueryObject esObject) {
        SearchBaseResult<Map<String, ESResponse>> SearchResult = new SearchBaseResult<>();

        final SearchRequestBuilder searchRequestBuilder = client.prepareSearch().setIndices(esObject.getIndexName());
        if (StringUtils.isNotBlank(esObject.getTypeName())) {
            searchRequestBuilder.setTypes(esObject.getTypeName());
        }

        final org.elasticsearch.search.builder.SearchSourceBuilder searchSourceBuilder = org.elasticsearch.search.builder.SearchSourceBuilder.searchSource();
        this.searchSourceBuilder.page(esObject.getPageCondition(), searchSourceBuilder);
        this.searchSourceBuilder.sort(esObject.getOrderConditions(), searchSourceBuilder);
        searchSourceBuilder.fetchSource(Boolean.TRUE);
        searchRequestBuilder.setSource(searchSourceBuilder);

        // 需要在 SearchSourceBuilder 之后添加
        final BoolQueryBuilder queryBuilder = queryConditionBuilder.innerBuilder(esObject.getSearchConditions(),
                esObject.getVersion(), esObject.getIndexName(), esObject.getTypeName());
        searchRequestBuilder.setQuery(queryBuilder);

        final CollapseBuilder collapseBuilder = collapseConditionBuilder.build(searchRequestBuilder, esObject);
        searchRequestBuilder.setCollapse(collapseBuilder);

        SearchResponse searchResponse;
        try {
            SearchLogger.log(searchRequestBuilder);
            searchResponse = searchRequestBuilder.execute().get();
            SearchLogger.log(searchResponse);
        } catch (Exception ex) {
            SearchLogger.error("elastic collapse error", ex);
            return SearchBaseResult.faild(ESErrorCode.ELASTIC_ERROR_CODE, "elastic error:" + ex.getMessage());
        }

        try {
            final ESResponse handlerResponse = esQueryResponseHandler.handler(esObject, searchResponse);
            final Map<String, ESResponse> handlerResult = collapseResponseHandler.handler(searchResponse, esObject);
            handlerResult.put("searchResult", handlerResponse);
            SearchResult.setResult(handlerResult);
            return SearchResult;
        } catch (Exception ex) {
            SearchLogger.error("escenter collapse response handle error", ex);
            return SearchBaseResult.faild(ESErrorCode.ESCENTER_ERROR_CODE, "escenter error:" + ex.getMessage());
        }
    }
}

