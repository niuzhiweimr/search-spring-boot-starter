package com.elastic.search.elasticsearch.condition;


import com.elastic.search.elasticsearch.config.IndexHelper;
import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;
import com.elastic.search.elasticsearch.dataobject.enums.ConditionExpressionEnum;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * <p>
 * 检索条件 构建器
 *
 * @author jack
 */
public class SearchConditionBuilder {

    private static final String KEYWORD = ".keyword";

    /**
     * 根据查询条件，构建基础查询
     *
     * @param condition 查询条件
     * @return 基础查询
     */
    public QueryBuilder builder(SearchCondition condition, IndexHelper helper) {

        String fieldName = condition.getFieldName();
        final Object singleValue = condition.getSingleValue();
        final boolean multipleValue = condition.getMultipleValue();
        final ConditionExpressionEnum expression = condition.getConditionExpression();

        QueryBuilder queryBuilder;
        switch (expression) {
            case EQUAL:
                fieldName = getFieldName(helper, fieldName, singleValue);
                queryBuilder = QueryBuilders.termQuery(fieldName, singleValue);
                break;
            case LESSER:
                queryBuilder = QueryBuilders.rangeQuery(fieldName).lt(singleValue);
                break;
            case GREATER:
                queryBuilder = QueryBuilders.rangeQuery(fieldName).gt(singleValue);
                break;
            case LESSER_OR_EQUAL:
                queryBuilder = QueryBuilders.rangeQuery(fieldName).lte(singleValue);
                break;
            case GREATER_OR_EQUAL:
                queryBuilder = QueryBuilders.rangeQuery(fieldName).gte(singleValue);
                break;
            case UNEQUAL:
                fieldName = getFieldName(helper, fieldName, singleValue);
                queryBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery(fieldName, singleValue));
                break;
            case LIKE:
                fieldName = getFieldName(helper, fieldName, singleValue);
                queryBuilder = QueryBuilders.wildcardQuery(fieldName, "*" + singleValue + "*");
                break;
            case NULL:
                queryBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(fieldName));
                break;
            case NOT_NULL:
                queryBuilder = QueryBuilders.existsQuery(fieldName);
                break;
            case IN:
                fieldName = getFieldName(helper, fieldName, condition.getFieldValues());
                queryBuilder = QueryBuilders.termsQuery(fieldName, condition.getFieldValues());
                break;
            case NOT_IN:
                fieldName = getFieldName(helper, fieldName, condition.getFieldValues());
                queryBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery(fieldName, condition.getFieldValues()));
                break;
            case BETWEEN:
                queryBuilder = QueryBuilders.boolQuery()
                        .must(QueryBuilders.rangeQuery(fieldName).gt(condition.getMinValue()).lt(condition.getMaxValue()));
                break;
            case BETWEEN_AND:
                queryBuilder = QueryBuilders.boolQuery()
                        .must(QueryBuilders.rangeQuery(fieldName).gte(condition.getMinValue()).lte(condition.getMaxValue()));
                break;
            case BETWEEN_LEFT:
                queryBuilder = QueryBuilders.boolQuery()
                        .must(QueryBuilders.rangeQuery(fieldName).gte(condition.getMinValue()).lt(condition.getMaxValue()));
                break;
            case BETWEEN_RIGHR:
                queryBuilder = QueryBuilders.boolQuery()
                        .must(QueryBuilders.rangeQuery(fieldName).gt(condition.getMinValue()).lte(condition.getMaxValue()));
                break;
            case MATCH:
                queryBuilder = QueryBuilders.matchQuery(fieldName, singleValue);
                break;
            default:
                throw new RuntimeException("表达不存在");
        }

        // 若是多值字段，需使用nestedQuery保证查询结果的准确性
        if (multipleValue) {
            if (fieldName.contains(".")) {
                fieldName = fieldName.substring(0, fieldName.lastIndexOf("."));
            }

            queryBuilder = QueryBuilders.nestedQuery(fieldName, queryBuilder, ScoreMode.None);
        }
        return queryBuilder;
    }

    public BoolQueryBuilder builderVersion2(SearchCondition condition, BoolQueryBuilder boolQueryBuilder,
                                            IndexHelper helper) {
        boolQueryBuilder.must(builder(condition, helper));
        if (condition.hasNext()) {
            builderVersion2(condition.next(), boolQueryBuilder, helper);
        }
        return boolQueryBuilder;
    }

    private String getFieldName(IndexHelper helper, String fieldName, Object singleValue) {
        if (helper != null && helper.isSupportTextAndKeywordField(fieldName)) {
            return fieldName + KEYWORD;
        }
        return fieldName;
    }

    private String getFieldName(IndexHelper helper, String fieldName, Object... fieldValues) {
        if (helper != null && helper.isSupportTextAndKeywordField(fieldName)) {
            return fieldName + KEYWORD;
        }
        return fieldName;
    }
}
