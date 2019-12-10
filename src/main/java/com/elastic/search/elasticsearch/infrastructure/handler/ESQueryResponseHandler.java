package com.elastic.search.elasticsearch.infrastructure.handler;


import com.elastic.search.elasticsearch.convert.ESSearchConvertor;
import com.elastic.search.elasticsearch.dataobject.CollapseQueryObject;
import com.elastic.search.elasticsearch.dataobject.ESDocument;
import com.elastic.search.elasticsearch.dataobject.ESResponse;
import com.elastic.search.elasticsearch.dataobject.QueryESObject;
import com.elastic.search.elasticsearch.dataobject.conditions.PageCondition;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author niuzhiwei
 */
public class ESQueryResponseHandler {

    public ESResponse handler(QueryESObject esObject, final SearchResponse searchResponse) {
        ESResponse esResponse = new ESResponse();
        esResponse.setIndexName(esObject.getIndexName());
        esResponse.setTypeName(esObject.getTypeName());
        // 检索文档处理
        documentHandler(esObject, searchResponse, esResponse);
        // 聚合结果解析
        aggregationHandler(esResponse, esObject, searchResponse);
        return esResponse;
    }

    public ESResponse handler(CollapseQueryObject esObject, final SearchResponse searchResponse) {
        ESResponse esResponse = new ESResponse();
        esResponse.setIndexName(esObject.getIndexName());
        esResponse.setTypeName(esObject.getTypeName());
        // 检索文档处理
        documentHandler(esObject, searchResponse, esResponse);
        return esResponse;
    }

    /**
     * 检索文档处理
     *
     * @param esObject       检索请求参数
     * @param searchResponse 检索请求响应结果
     * @param esResponse     ES 检索响应
     */
    private void documentHandler(QueryESObject esObject, final SearchResponse searchResponse, ESResponse esResponse) {

        final SearchHits hits = searchResponse.getHits();

        if (null == hits) {
            return;
        }

        // 设置分页响应参数
        if (esObject.pageSearch()) {
            final PageCondition pageCondition = esObject.getPageCondition();
            pageCondition.setTotalDocs(hits.getTotalHits());
            esResponse.setPageCondition(pageCondition);
        }

        final SearchHit[] searchHits = hits.getHits();
        if (null == searchHits || searchHits.length == 0) {
            return;
        }

        List<ESDocument> esDocuments = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            Map dataMap = null;
            try {
                dataMap = ESSearchConvertor.json2Object(searchHit.getSourceAsString(), Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (dataMap != null && !dataMap.isEmpty()) {
                ESDocument esDocument = new ESDocument();
                final String docId = searchHit.getId();
                esDocument.setDataMap(dataMap);
                esDocuments.add(esDocument);
            }
        }
        esResponse.setEsDocuments(esDocuments);
    }

    /**
     * 检索文档处理
     *
     * @param esObject       检索请求参数
     * @param searchResponse 检索请求响应结果
     * @param esResponse     ES 检索响应
     */
    private void documentHandler(CollapseQueryObject esObject, final SearchResponse searchResponse,
                                 ESResponse esResponse) {
        final SearchHits hits = searchResponse.getHits();
        if (null == hits) {
            return;
        }

        // 设置分页响应参数
        if (esObject.pageSearch()) {
            final PageCondition pageCondition = esObject.getPageCondition();
            pageCondition.setTotalDocs(hits.getTotalHits());
            esResponse.setPageCondition(pageCondition);
        }

        final SearchHit[] searchHits = hits.getHits();
        if (null == searchHits || searchHits.length == 0) {
            return;
        }

        List<ESDocument> esDocuments = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            Map dataMap = null;
            try {
                dataMap = ESSearchConvertor.json2Object(searchHit.getSourceAsString(), Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (dataMap != null && !dataMap.isEmpty()) {
                ESDocument esDocument = new ESDocument();
                final String docId = searchHit.getId();
                esDocument.setDataMap(dataMap);
                esDocuments.add(esDocument);
            }
        }
        esResponse.setEsDocuments(esDocuments);
    }

    /**
     * 聚合结果分析处理
     *
     * @param esResponse     ES 通用响应结果
     * @param esObject       ES 通用查询参数
     * @param searchResponse ES 查询响应结果
     */
    private void aggregationHandler(ESResponse esResponse, QueryESObject esObject, final SearchResponse searchResponse) {
        // 没有进行分组查询
        if (!esObject.groupSearch()) {
            return;
        }
        Map<String, List<Map<String, Object>>> countCesult = new HashMap<>();
        aggregation(searchResponse.getAggregations(), "", countCesult, esObject.getFunctionNames());
        esResponse.setCountResult(countCesult);
    }

    /**
     * 递归到最后的一个聚合桶结束
     *
     * @param aggregations  聚合分析结果
     * @param resultKey     分组key
     * @param countCesult   聚合结果解析
     * @param functionNames 聚合函数名字
     */
    private void aggregation(final Aggregations aggregations, String resultKey,
                             Map<String, List<Map<String, Object>>> countCesult, String[] functionNames) {
        if (aggregations == null) {
            return;
        }
        // 1:获取当前聚合分析的所有聚合分析结果集
        final List<Aggregation> aggregationsList = aggregations.asList();
        // 2:没有分组，使用聚合函数，key为""
        String key = "";
        for (Aggregation aggregation : aggregationsList) {
            // sql 字段分组聚合类型
            if (aggregation instanceof StringTerms) {
                StringTerms stringTerms = ((StringTerms) aggregation);
                final List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
                for (Terms.Bucket bucket : buckets) {
                    // 聚合后每个桶的名字
                    key = bucket.getKeyAsString();
                    final Aggregations innerAggregations = bucket.getAggregations();
                    // 依据当前聚合桶内部嵌套的聚合类型判断，是否到了最后一个子聚合
                    final List<Aggregation> inneraggregationsList = innerAggregations.asList();
                    Aggregation innerAggregation = inneraggregationsList.get(0);
                    if (!(innerAggregation instanceof StringTerms)) {
                        for (Aggregation floorInnerAggregation : inneraggregationsList) {
                            wrapCountResult(floorInnerAggregation, resultKey + key, countCesult);
                        }
                    } else {
                        // 依据分组聚合，使用"_"分隔不同的组合关系
                        key += "_";
                        aggregation(innerAggregations, resultKey + key, countCesult, functionNames);
                    }
                }
            } else {// 没有分组时，直接进行统计结果的封装操作
                wrapCountResult(aggregation, resultKey + key, countCesult);
            }
        }
    }

    /**
     * 获取聚合结果，进行统一封装 //@param functionNames 检索提供的函数别名 //@param innerAggregations
     * es 聚合底层聚合结果
     *
     * @param key         分组组合key
     * @param countCesult 统一统计结果
     */
    private void wrapCountResult(Aggregation agregation, String key, Map<String, List<Map<String, Object>>> countCesult) {

        if (agregation instanceof NumericMetricsAggregation.SingleValue) {// max
            // ,min,sum
            final NumericMetricsAggregation.SingleValue numericProperty = (NumericMetricsAggregation.SingleValue) agregation;

            final String functionName = numericProperty.getName();
            final double value = numericProperty.value();

            final HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put(key, value);

            List<Map<String, Object>> groupList = countCesult.get(functionName);
            if (groupList == null) {
                groupList = new ArrayList<>();
            }
            groupList.add(stringObjectHashMap);

            countCesult.put(functionName, groupList);
        }
    }

    /**
     * 将es搜索返回属性，封装成嵌套map格式，主要针对具有引用类型数据时使用
     *
     * @param hitField es 扁平存储属性
     * @param rootMap es 检索结果响应数据
     */
//    private void nestedMap(final SearchHitField hitField, Map rootMap) {
//        final String fieldName = hitField.getName();
//        final Object hitFieldValue = hitField.getValues();
//        if (fieldName.contains(".")) {
//            final String[] split = fieldName.split("\\.");
//            Map<Object, Object> currentMap = rootMap;
//            final int length = split.length;
//            for (int i = 0; i < length; i++) {
//                final String str = split[i];
//                Map<Object, Object> term = currentMap.containsKey(str) ? (Map) currentMap.get(str) : new HashMap<>();
//                currentMap.put(str, term);
//                currentMap = term;
//                if (i + 2 == length) {
//                    currentMap.put(split[i + 1], hitFieldValue);
//                    break;
//                }
//            }
//        } else {
//            rootMap.put(fieldName, hitFieldValue);
//        }
//    }
}
