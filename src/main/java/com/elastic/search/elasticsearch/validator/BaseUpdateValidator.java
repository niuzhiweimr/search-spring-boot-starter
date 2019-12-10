package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.domain.Status;
import com.elastic.search.elasticsearch.dataobject.NestedESObject;
import com.elastic.search.elasticsearch.dataobject.UpdateESObject;
import com.elastic.search.elasticsearch.dataobject.enums.OperateTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author niuzhiwei
 */
@Slf4j
public class BaseUpdateValidator extends BaseValidator {

    public SearchBaseResult<Boolean> validate(UpdateESObject obj) {

        final SearchBaseResult<Boolean> baseValidateResult = super.baseValidate(obj);
        if (baseValidateResult.isFailed()) {
            log.warn("es base request param error ,systemName or indexName or typeName should not null!");
            return baseValidateResult;
        }

        final Map<Object, Object> dataMap = obj.getDataMap();
        if (dataMap == null) {
            log.warn("update es object ,dataMap should not null!");
            baseValidateResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【dataMap】为空"));
        }

        final NestedESObject nestedESObject = obj.getNestedESObject();
        final OperateTypeEnum nestedOperateType = obj.getNestedOperateType();

        if (nestedESObject != null) {
            final String fieldName = nestedESObject.getFieldName();
            if (fieldName == null) {
                log.warn("nested update es object ,fieldName should not null!");
                baseValidateResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "嵌套更新请求参数【fieldName】为空"));
            }

            if (nestedOperateType == null) {
                log.warn("nested update es object ,operate type should not null!");
                baseValidateResult
                        .setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "嵌套更新请求参数【nestedOperateType】为空"));
            }

            if (!nestedESObject.isList()) {
                final NestedESObject nextNestedESObject = nestedESObject.getNextNestedESObject();
                if (nextNestedESObject == null) {
                    log.warn("nested update es ool object ,nextNestedESObject should not null!");
                    baseValidateResult
                            .setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE,
                                    "嵌套更新顶级非集合请求参数【nextNestedESObject】为空"));
                }
            }

        }
        return baseValidateResult;
    }
}
