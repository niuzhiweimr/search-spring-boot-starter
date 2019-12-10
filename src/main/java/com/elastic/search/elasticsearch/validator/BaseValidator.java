package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.domain.Status;
import com.elastic.search.elasticsearch.dataobject.BaseESObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author niuzhiwei
 */
@Slf4j
public class BaseValidator {

    public SearchBaseResult<Boolean> baseValidate(BaseESObject baseObj) {
        SearchBaseResult<Boolean> dataResult = new SearchBaseResult<>();
        if (null == baseObj) {
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "hase requesr is null"));
        }

        final String systemName = baseObj.getSystemName();
        if (StringUtils.isBlank(systemName)) {
            log.warn("es search base request parameter systemName is null !");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "request systemName is null "));
        }

        final String indexName = baseObj.getIndexName();
        if (StringUtils.isBlank(indexName)) {
            log.warn("es search base  indexName is null ");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "request indexName is null "));
        }
        final String typeName = baseObj.getTypeName();
        if (StringUtils.isBlank(typeName)) {
            log.warn("es search typeName is null");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "request typeName is null "));
        }
        return dataResult;
    }
}
