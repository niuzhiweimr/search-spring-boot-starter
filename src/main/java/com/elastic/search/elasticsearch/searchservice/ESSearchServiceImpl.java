package com.elastic.search.elasticsearch.searchservice;


import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.elasticsearch.dataobject.*;
import com.elastic.search.elasticsearch.service.ESNestedSearchService;
import com.elastic.search.elasticsearch.validator.*;
import com.sinoiov.search.elasticsearch.dataobject.*;
import com.sinoiov.search.elasticsearch.validator.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * @author niuzhiwei
 */
public class ESSearchServiceImpl implements ESSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESSearchServiceImpl.class);

    @Autowired
    private ESSaveValidator esSaveValidator;

    @Autowired
    private ESQueryValidator esQueryValidator;

    @Autowired
    private ESDeleteValidator esDeleteValidator;

    @Autowired
    private ESUpdateValidator esUpdateValidator;

    @Autowired
    private ESBatchSaveValidator esBatchSaveValidator;

    @Autowired
    private ESBatchUpdateValidator esBatchUpdateValidator;

    @Autowired
    private ESBatchDeleteValidator esBatchDeleteValidator;

    @Autowired
    private ESConditionUpdateValidator esConditionUpdateValidator;

    @Autowired
    private ESConditionDeleteValidator esConditionDeleteValidator;

    @Resource
    private ESNestedSearchService esNestedSearchService;

    @Value("${es.auto.refresh:true}")
    private Boolean autoRefresh;

    @Override
    public SearchBaseResult<Boolean> esSave(SaveESObject obj) {
        LOGGER.debug("esclient index request param:{}.", obj);
        final SearchBaseResult<Boolean> SearchResult = esSaveValidator.validate(obj);
        if (SearchResult.isFailed()) {
            return SearchResult;
        }
        obj.setRefresh(autoRefresh);
        return esNestedSearchService.save(obj);
    }

    @Override
    public SearchBaseResult<ESResponse> esQuery(QueryESObject obj) {
        LOGGER.debug("esclient query req param:{}.", obj);
        final SearchBaseResult<Boolean> validateResult = esQueryValidator.validate(obj);
        if (validateResult.isFailed()) {
            LOGGER.warn("esclient query req param error, illegal param:{}.", obj);
            SearchBaseResult<ESResponse> SearchResult = new SearchBaseResult<>();
            SearchResult.setStatus(validateResult.getStatus());
            return SearchResult;
        }
        return esNestedSearchService.query(obj);
    }

    @Override
    public SearchBaseResult<Boolean> esDelete(DeleteESObject obj) {
        LOGGER.debug("es delete , param:{}.", obj);
        final SearchBaseResult<Boolean> SearchResult = esDeleteValidator.validate(obj);
        if (SearchResult.isFailed()) {
            return SearchResult;
        }
        obj.setRefresh(autoRefresh);
        return esNestedSearchService.delete(obj);
    }

    @Override
    public SearchBaseResult<Boolean> esUpdate(UpdateESObject obj) {
        LOGGER.debug("es update , param:{}.", obj);
        final SearchBaseResult<Boolean> SearchResult = esUpdateValidator.validate(obj);
        if (SearchResult.isFailed()) {
            return SearchResult;
        }
        obj.setRefresh(autoRefresh);
        return esNestedSearchService.update(obj);
    }

    @Override
    public SearchBaseResult<Boolean> esBatchSave(BatchSaveESObject obj) {
        LOGGER.debug("es batch save , param:{}.", obj);
        final SearchBaseResult<Boolean> SearchResult = esBatchSaveValidator.validate(obj);
        if (SearchResult.isFailed()) {
            return SearchResult;
        }
        obj.setRefresh(autoRefresh);
        return esNestedSearchService.batchSave(obj);
    }

    @Override
    public SearchBaseResult<Boolean> esBatchUpdate(BatchUpdateESObject obj) {
        LOGGER.debug("es batch update , param:{}.", obj);
        final SearchBaseResult<Boolean> SearchResult = esBatchUpdateValidator.validate(obj);
        if (SearchResult.isFailed()) {
            return SearchResult;
        }
        obj.setRefresh(autoRefresh);
        return esNestedSearchService.batchUpdate(obj);
    }

    @Override
    public SearchBaseResult<Boolean> esBatchDelete(BatchDeleteESObject obj) {
        LOGGER.debug("es batch delete , param:{}.", obj);
        final SearchBaseResult<Boolean> SearchResult = esBatchDeleteValidator.validate(obj);
        if (SearchResult.isFailed()) {
            return SearchResult;
        }
        obj.setRefresh(autoRefresh);
        return esNestedSearchService.batchDelete(obj);
    }

    @Override
    public SearchBaseResult<Boolean> conditionUpdate(ConditionUpdateESObject obj) {
        LOGGER.debug("es condition update , param:{}.", obj);
        final SearchBaseResult<Boolean> SearchResult = esConditionUpdateValidator.validate(obj);
        if (SearchResult.isFailed()) {
            return SearchResult;
        }
        obj.setRefresh(autoRefresh);
        return esNestedSearchService.conditionUpdate(obj);
    }

    @Override
    public SearchBaseResult<Boolean> conditionDelete(ConditionDeleteESObject obj) {
        LOGGER.debug("es condition delete , param:{}.", obj);
        final SearchBaseResult<Boolean> SearchResultResult = esConditionDeleteValidator.validate(obj);
        if (SearchResultResult.isFailed()) {
            return SearchResultResult;
        }
        obj.setRefresh(autoRefresh);
        return esNestedSearchService.conditionDelete(obj);
    }
}
