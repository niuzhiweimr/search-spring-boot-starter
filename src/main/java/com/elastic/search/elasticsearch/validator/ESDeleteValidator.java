package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.elasticsearch.dataobject.DeleteESObject;

/**
 * @author niuzhiwei
 */
public class ESDeleteValidator extends BaseValidator {

    public SearchBaseResult<Boolean> validate(DeleteESObject obj) {
        final SearchBaseResult<Boolean> baseValidateResult = super.baseValidate(obj);

        return baseValidateResult;
    }
}
