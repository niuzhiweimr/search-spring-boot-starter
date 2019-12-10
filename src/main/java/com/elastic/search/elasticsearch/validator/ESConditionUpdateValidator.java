package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.domain.Status;
import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;
import com.elastic.search.elasticsearch.dataobject.ConditionUpdateESObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niuzhiwei
 */
@Slf4j
public class ESConditionUpdateValidator extends BaseUpdateValidator {

    @Resource
    private ESUpdateValidator esUpdateValidator;

    public SearchBaseResult<Boolean> validate(ConditionUpdateESObject obj) {
        SearchBaseResult<Boolean> dataResult = new SearchBaseResult<>();
        if (null == obj) {
            log.warn("条件更新请求参数为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "条件更新请求参数为空"));
        }

        final List<SearchCondition> conditions = obj.getConditions();
        if (CollectionUtils.isEmpty(conditions)) {
            log.warn("条件更新[conditions]数据为空.");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "条件更新请求参数【conditions】为空"));
        }

        if (dataResult.isFailed()) {
            return dataResult;
        }

        return super.validate(obj);
    }
}
