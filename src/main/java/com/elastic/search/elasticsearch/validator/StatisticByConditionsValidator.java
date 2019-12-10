package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.domain.Status;
import com.elastic.search.elasticsearch.dataobject.StatisticESObject;
import com.elastic.search.elasticsearch.dataobject.conditions.FunctionCondition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author niuzhiwei
 */
@Slf4j
public class StatisticByConditionsValidator {

    public SearchBaseResult<Map<String, Number>> validate(StatisticESObject obj) {
        SearchBaseResult<Map<String, Number>> dataResult = new SearchBaseResult<>();
        if (obj == null) {
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "统计服务，请求参数不得为空"));
        }

        final String systemName = obj.getSystemName();
        if (StringUtils.isBlank(systemName)) {
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "统计服务，【systemName】不得为空"));
        }

        final String indexName = obj.getIndexName();
        if (StringUtils.isBlank(indexName)) {
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "统计服务，【indexName】不得为空"));
        }

        final List<FunctionCondition> functionConditions = obj.getFunctionConditions();
        if (CollectionUtils.isEmpty(functionConditions)) {
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "统计服务，【functionConditions】不得为空"));
        }

        return dataResult;
    }
}
