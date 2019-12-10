package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.domain.Status;
import com.elastic.search.elasticsearch.dataobject.SaveESObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * es保存数据验证
 *
 * @author niuzhiwei
 */
@Slf4j
public class ESSaveValidator extends BaseValidator {


    public SearchBaseResult<Boolean> validate(SaveESObject obj) {
        final SearchBaseResult<Boolean> baseValidateResult = super.baseValidate(obj);
        if (baseValidateResult.isFailed()) {
            return baseValidateResult;
        }

        final Map<?, ?> dataMap = obj.getDataMap();
        if (dataMap == null || dataMap.isEmpty()) {
            log.warn("save es object ,dataMap is null!");
            baseValidateResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "存储ES, dataMap 为空"));
        }

        return baseValidateResult;
    }
}
