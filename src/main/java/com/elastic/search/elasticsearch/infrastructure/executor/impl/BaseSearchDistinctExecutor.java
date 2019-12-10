package com.elastic.search.elasticsearch.infrastructure.executor.impl;


import com.elastic.search.common.boot.SearchBeanContext;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.exception.SystemException;
import com.elastic.search.elasticsearch.convert.ESSearchConvertor;
import com.elastic.search.elasticsearch.dataobject.CollapseQueryObject;
import com.elastic.search.elasticsearch.dataobject.ESDocument;
import com.elastic.search.elasticsearch.dataobject.ESResponse;
import com.elastic.search.elasticsearch.dataobject.conditions.InnerHitsCondition;
import com.elastic.search.elasticsearch.dataobject.conditions.OrderCondition;
import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;
import com.elastic.search.elasticsearch.infrastructure.common.SearchAdapter;
import com.elastic.search.elasticsearch.infrastructure.consts.FrameworkExceptionConstants;
import com.elastic.search.elasticsearch.infrastructure.executor.SearchExecutor;
import com.elastic.search.elasticsearch.searchservice.ESStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @author niuzhiwei
 */
@Slf4j
public abstract class BaseSearchDistinctExecutor<T> extends SearchAdapter<CollapseQueryObject> implements SearchExecutor {


    /**
     * 根据条件查询列表，单字段排序，含有排序功能
     *
     * @param searchConditions
     * @param fieldName
     * @param orderConditions
     * @return
     * @throws SystemException
     */
    public synchronized List<T> list(String fieldName, List<SearchCondition> searchConditions, List<OrderCondition> orderConditions) throws SystemException {
        try {
            CollapseQueryObject obj = this.setConfig(this.getConfig(), CollapseQueryObject.class.newInstance());

            obj.setFieldName(fieldName);
            obj.setSearchConditions(searchConditions);
            obj.setOrderConditions(orderConditions);
            List<InnerHitsCondition> innerHitsConditions = new ArrayList<InnerHitsCondition>();
            InnerHitsCondition inner = new InnerHitsCondition();
            inner.setHitName("hitName");
            inner.setFieldNames(this.getNeedFields(this.getGenericActualType(this.getClass())));
            innerHitsConditions.add(inner);
            obj.setInnerHitsConditions(innerHitsConditions);
            SearchBaseResult<Map<String, ESResponse>> result = SearchBeanContext.getBean(ESStatisticService.class).collapse(obj);
            if (result.isSuccess()) {
                if (null != result.getResult() && result.getResult().size() > 0) {
                    List<T> list = new ArrayList<T>();
                    ESResponse response = result.getResult().get(inner.getHitName());
                    if (null != response.getEsDocuments() && response.getEsDocuments().size() > 0) {
                        List<ESDocument> docs = response.getEsDocuments();
                        for (ESDocument doc : docs) {
                            list.add(ESSearchConvertor.map2Object(doc.getDataMap(), this.getGenericActualType(this.getClass())));
                        }
                        return list;
                    }
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据条件查询列表): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 根据条件查询列表
     *
     * @param searchConditions
     * @param fieldName
     * @return
     * @throws SystemException
     */
    public synchronized List<T> list(String fieldName, List<SearchCondition> searchConditions) throws SystemException {
        try {
            CollapseQueryObject obj = this.setConfig(this.getConfig(), CollapseQueryObject.class.newInstance());

            obj.setFieldName(fieldName);
            List<InnerHitsCondition> innerHitsConditions = new ArrayList<InnerHitsCondition>();
            InnerHitsCondition inner = new InnerHitsCondition();
            inner.setHitName("hitName");
            inner.setFieldNames(this.getNeedFields(this.getGenericActualType(this.getClass())));
            innerHitsConditions.add(inner);
            obj.setInnerHitsConditions(innerHitsConditions);
            SearchBaseResult<Map<String, ESResponse>> result = SearchBeanContext.getBean(ESStatisticService.class).collapse(obj);
            if (result.isSuccess()) {
                if (null != result.getResult() && result.getResult().size() > 0) {
                    List<T> list = new ArrayList<T>();
                    ESResponse response = result.getResult().get(inner.getHitName());
                    if (null != response && null != response.getEsDocuments() && response.getEsDocuments().size() > 0) {
                        List<ESDocument> docs = response.getEsDocuments();
                        for (ESDocument doc : docs) {
                            list.add(ESSearchConvertor.map2Object(doc.getDataMap(), this.getGenericActualType(this.getClass())));
                        }
                        return list;
                    }
                }
                return null;
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, ToStringBuilder.reflectionToString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据条件查询列表): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }
}
