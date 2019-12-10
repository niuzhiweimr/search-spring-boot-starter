package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.domain.Status;
import com.elastic.search.elasticsearch.dataobject.BatchSaveESObject;
import com.elastic.search.elasticsearch.dataobject.SaveESObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author niuzhiwei
 */
@Slf4j
public class ESBatchSaveValidator extends BaseValidator {

    @Resource
    private ESSaveValidator esSaveValidator;

    public SearchBaseResult<Boolean> validate(BatchSaveESObject obj) {
        SearchBaseResult<Boolean> dataResult = new SearchBaseResult<>();
        if (null == obj) {
            log.warn("批量新增请求参数为空.");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "批量新增请求参数为空"));
        }
        final List<SaveESObject> saveDatas = obj.getSaveDatas();
        if (CollectionUtils.isEmpty(saveDatas)) {
            log.warn("批量新增[saveDatas]数据为空.");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "批量新增请求参数【saveDatas】为空"));
        }

        if (dataResult.isFailed()) {
            return dataResult;
        }

        for (SaveESObject saveData : saveDatas) {
            final SearchBaseResult<Boolean> validateResult = esSaveValidator.validate(saveData);
            if (validateResult.isFailed()) {
                return validateResult;
            }
        }

        return dataResult;
    }
}
