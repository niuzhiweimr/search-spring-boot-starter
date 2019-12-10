package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.domain.Status;
import com.elastic.search.elasticsearch.dataobject.BatchDeleteESObject;
import com.elastic.search.elasticsearch.dataobject.DeleteESObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niuzhiwei
 */
@Slf4j
public class ESBatchDeleteValidator extends BaseValidator {

    @Resource
    private ESDeleteValidator esDeleteValidator;

    public SearchBaseResult<Boolean> validate(BatchDeleteESObject obj) {
        SearchBaseResult<Boolean> dataResult = new SearchBaseResult<>();
        if (null == obj) {
            log.warn("批量删除请求参数为空.");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "批量删除请求参数为空"));
        }
        final List<DeleteESObject> deleteDatas = obj.getDeleteDatas();
        if (CollectionUtils.isEmpty(deleteDatas)) {
            log.warn("批量删除[deleteDatas]数据为空.");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "批量删除请求参数【deleteDatas】为空"));
        }

        for (DeleteESObject deleteData : deleteDatas) {
            final SearchBaseResult<Boolean> validateResult = esDeleteValidator.validate(deleteData);
            if (validateResult.isFailed()) {
                return validateResult;
            }
        }
        return dataResult;
    }
}
