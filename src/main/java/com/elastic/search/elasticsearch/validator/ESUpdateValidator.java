package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.domain.Status;
import com.elastic.search.elasticsearch.dataobject.UpdateESObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author niuzhiwei
 */
@Slf4j
public class ESUpdateValidator extends BaseUpdateValidator {

    @Override
    public SearchBaseResult<Boolean> validate(UpdateESObject obj) {
        final SearchBaseResult<Boolean> superValidateResult = super.validate(obj);
        if (superValidateResult.isFailed()) {
            return superValidateResult;
        }
        final Map<Object, Object> ukMap = obj.getUkMap();
        if (ukMap == null || ukMap.isEmpty()) {
            log.warn("update es object ,ukMap should not null!");
            superValidateResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "更新ES,ukMap为空"));
        }
        return superValidateResult;
    }
}
