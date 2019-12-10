package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;
import com.elastic.search.elasticsearch.dataobject.enums.ConditionExpressionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author niuzhiwei
 */
@Slf4j
public class SearchConditionValidator {

    public SearchBaseResult<Boolean> validator(final List<SearchCondition> searchConditions) {
        if (CollectionUtils.isEmpty(searchConditions)) {
            return SearchBaseResult.success(Boolean.TRUE, Boolean.class);
        }
        for (SearchCondition searchCondition : searchConditions) {
            final Object[] fieldValues = searchCondition.getFieldValues();
            final String singleValue =
                    searchCondition.getSingleValue() == null ? "" : String.valueOf(searchCondition.getSingleValue());
            final String minValue = searchCondition.getMinValue();
            final String maxValue = searchCondition.getMaxValue();
            final ConditionExpressionEnum conditionExpression = searchCondition.getConditionExpression();
            switch (conditionExpression) {
                case NOT_IN:
                    if (fieldValues == null || fieldValues.length == 0) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【NOT_IN】时【fieldValues】不得为空");
                    }
                    break;
                case IN:
                    if (fieldValues == null || fieldValues.length == 0) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【IN】时【fieldValues】不得为空");
                    }
                    break;
                case LIKE:
                    if (StringUtils.isBlank(singleValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【LIKE】时【singleValue】不得为空");
                    }
                    break;
                case LESSER:
                    if (StringUtils.isBlank(singleValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【LESSER】时【singleValue】不得为空");
                    }
                    break;
                case UNEQUAL:
                    if (StringUtils.isBlank(singleValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【UNEQUAL】时【singleValue】不得为空");
                    }
                    break;
                case GREATER:
                    if (StringUtils.isBlank(singleValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【GREATER】时【singleValue】不得为空");
                    }
                    break;
                case LESSER_OR_EQUAL:
                    if (StringUtils.isBlank(singleValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【LESSER_OR_EQUAL】时【singleValue】不得为空");
                    }
                    break;
                case GREATER_OR_EQUAL:
                    if (StringUtils.isBlank(singleValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【GREATER_OR_EQUAL】时【singleValue】不得为空");
                    }
                    break;
                case EQUAL:
                    if (StringUtils.isBlank(singleValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【EQUAL】时【singleValue】不得为空");
                    }
                    break;
                case BETWEEN_RIGHR:
                    if (StringUtils.isBlank(minValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN_RIGHR】时【minValue】不得为空");
                    }
                    if (StringUtils.isBlank(maxValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN_RIGHR】时【maxValue】不得为空");
                    }
                    break;
                case BETWEEN:
                    if (StringUtils.isBlank(minValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN】时【minValue】不得为空");
                    }
                    if (StringUtils.isBlank(maxValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN】时【maxValue】不得为空");
                    }
                    break;
                case BETWEEN_LEFT:
                    if (StringUtils.isBlank(minValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN_LEFT】时【minValue】不得为空");
                    }
                    if (StringUtils.isBlank(maxValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN_LEFT】时【maxValue】不得为空");
                    }
                    break;
                case BETWEEN_AND:
                    if (StringUtils.isBlank(minValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN_AND】时【minValue】不得为空");
                    }
                    if (StringUtils.isBlank(maxValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【BETWEEN_AND】时【maxValue】不得为空");
                    }
                    break;
                case NULL:
                    break;
                case NOT_NULL:
                    break;
                case MATCH:
                    if (StringUtils.isBlank(singleValue)) {
                        return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                "查询请求参数为【MATCH】时【singleValue】不得为空");
                    }
                    break;
                default:
                    return SearchBaseResult.faild(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                            "搜索表达式暂不支持");
            }
        }
        return SearchBaseResult.success(Boolean.TRUE, Boolean.class);
    }
}
