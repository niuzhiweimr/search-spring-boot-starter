package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.domain.Status;
import com.elastic.search.elasticsearch.dataobject.ConditionDeleteESObject;
import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niuzhiwei
 */
public class ESConditionDeleteValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESConditionUpdateValidator.class);

    @Resource
    private ESDeleteValidator esDeleteValidator;

    public SearchBaseResult<Boolean> validate(ConditionDeleteESObject obj) {
        SearchBaseResult<Boolean> dataResult = new SearchBaseResult<>();
        if (null == obj) {
            LOGGER.warn("条件删除请求参数为空.");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "条件删除请求参数为空"));
        }

        final List<SearchCondition> conditions = obj.getConditions();
        if (CollectionUtils.isEmpty(conditions)) {
            LOGGER.warn("条件删除[conditions]数据为空.");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "条件删除请求参数【conditions】为空"));
        }

        if (dataResult.isFailed()) {
            return dataResult;
        }

        return esDeleteValidator.validate(obj);
    }
}
