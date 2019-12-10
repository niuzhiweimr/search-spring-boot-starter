package com.elastic.search.elasticsearch.validator;


import com.elastic.search.common.domain.ESErrorCode;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.domain.Status;
import com.elastic.search.elasticsearch.dataobject.CollapseQueryObject;
import com.elastic.search.elasticsearch.dataobject.conditions.InnerHitsCondition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author niuzhiwei
 */
@Slf4j
public class CollapseQueryValidator {

    public SearchBaseResult<Boolean> validator(CollapseQueryObject esObject) {
        SearchBaseResult<Boolean> dataResult = new SearchBaseResult<>();

        if (null == esObject) {
            log.warn("瓦解查询请求参数为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数为空"));
        }

        final String systemName = esObject.getSystemName();
        if (StringUtils.isBlank(systemName)) {
            log.warn("瓦解查询请求参数【systemName】为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【systemName】为空"));
        }

        final String indexName = esObject.getIndexName();
        if (StringUtils.isBlank(indexName)) {
            log.warn("瓦解查询请求参数【indexName】为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【indexName】为空"));
        }

        final String fieldName = esObject.getFieldName();
        if (StringUtils.isBlank(fieldName)) {
            log.warn("瓦解查询请求参数【fieldName】为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【fieldName】为空"));
        }

        final List<InnerHitsCondition> innerHitsConditions = esObject.getInnerHitsConditions();
        if (CollectionUtils.isEmpty(innerHitsConditions)) {
            log.warn("瓦解查询请求参数【innerHitsConditions】为空");
            dataResult.setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【innerHitsConditions】为空"));
        }

        for (InnerHitsCondition innerHitsCondition : innerHitsConditions) {
            final String hitName = innerHitsCondition.getHitName();
            if (StringUtils.isBlank(hitName)) {
                log.warn("瓦解查询请求参数【InnerHitsCondition#hitName】为空");
                dataResult
                        .setStatus(new Status(ESErrorCode.REQUEST_PARAM_ERROR_CODE, "请求参数【InnerHitsCondition#hitName】为空"));
            }
        }

        return dataResult;
    }
}
