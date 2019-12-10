package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.domain.Status;
import com.elastic.search.elasticsearch.dataobject.BatchUpdateESObject;
import com.elastic.search.elasticsearch.dataobject.UpdateESObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niuzhiwei
 */
@Slf4j
public class ESBatchUpdateValidator extends BaseValidator {

    @Resource
    private ESUpdateValidator esUpdateValidator;

    public SearchBaseResult<Boolean> validate(BatchUpdateESObject obj) {
        SearchBaseResult<Boolean> dataResult = new SearchBaseResult<>();
        if (null == obj) {
            log.warn("批量更新请求参数为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "批量更新请求参数为空"));
        }
        final List<UpdateESObject> updateDatas = obj.getUpdateDatas();
        if (CollectionUtils.isEmpty(updateDatas)) {
            log.warn("批量更新[updateDatas]数据为空.");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "批量更新请求参数【updateDatas】为空"));
        }

        if (dataResult.isFailed()) {
            return dataResult;
        }

        for (UpdateESObject updateData : updateDatas) {
            final SearchBaseResult<Boolean> validateResult = esUpdateValidator.validate(updateData);
            if (validateResult.isFailed()) {
                return validateResult;
            }
        }

        return dataResult;
    }
}
