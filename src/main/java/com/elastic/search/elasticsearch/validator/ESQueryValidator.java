package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.elasticsearch.dataobject.QueryESObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

@Slf4j
public class ESQueryValidator extends BaseValidator {

    @Resource
    private SearchConditionValidator searchConditionValidator;

    private static final Logger LOGGER = LoggerFactory.getLogger(ESQueryValidator.class);

    public SearchBaseResult<Boolean> validate(QueryESObject obj) {
        final SearchBaseResult<Boolean> baseValidateResult = super.baseValidate(obj);
        if (baseValidateResult.isFailed()) {
            return baseValidateResult;
        }
        return searchConditionValidator
                .validator(obj.getSearchConditions());
    }
}
