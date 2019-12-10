package com.elastic.search.elasticsearch.searchservice;


import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.elasticsearch.service.ESStatisticOperatorService;
import com.elastic.search.elasticsearch.dataobject.CollapseQueryObject;
import com.elastic.search.elasticsearch.dataobject.ESResponse;
import com.elastic.search.elasticsearch.dataobject.StatisticESObject;
import com.elastic.search.elasticsearch.validator.CollapseQueryValidator;
import com.elastic.search.elasticsearch.validator.StatisticByConditionsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author niuzhiwei
 */
public class ESStatisticServiceImpl implements ESStatisticService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESStatisticServiceImpl.class);

    @Autowired
    private StatisticByConditionsValidator statisticByConditionsValidator;

    @Autowired
    private CollapseQueryValidator collapseQueryValidator;

    @Autowired
    private ESStatisticOperatorService esStatisticOperatorService;

    @Override
    public SearchBaseResult<Map<String, Number>> statisticByConditions(StatisticESObject esObject) {
        LOGGER.info("statistic service request param:{}.", esObject);
        final SearchBaseResult<Map<String, Number>> validateResult = statisticByConditionsValidator.validate(esObject);
        if (validateResult.isFailed()) {
            return validateResult;
        }
        return esStatisticOperatorService.statisticByConditions(esObject);
    }

    @Override
    public SearchBaseResult<Map<String, ESResponse>> collapse(CollapseQueryObject esObject) {
        LOGGER.info("collapse service request param:{}.", esObject);

        final SearchBaseResult<Boolean> validateResult = collapseQueryValidator.validator(esObject);
        if (validateResult.isFailed()) {
            final SearchBaseResult<Map<String, ESResponse>> dataResult = new SearchBaseResult<>();
            dataResult.setStatus(validateResult.getStatus());
            return dataResult;
        }
        return esStatisticOperatorService.collapse(esObject);
    }
}
