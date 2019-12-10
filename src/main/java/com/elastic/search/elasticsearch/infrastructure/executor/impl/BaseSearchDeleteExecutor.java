package com.elastic.search.elasticsearch.infrastructure.executor.impl;


import com.elastic.search.common.boot.SearchBeanContext;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.exception.FrameworkException;
import com.elastic.search.elasticsearch.dataobject.BatchDeleteESObject;
import com.elastic.search.elasticsearch.dataobject.ConditionDeleteESObject;
import com.elastic.search.elasticsearch.dataobject.DeleteESObject;
import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;
import com.elastic.search.elasticsearch.infrastructure.common.SearchAdapter;
import com.elastic.search.elasticsearch.infrastructure.conf.BaseTypeIndexConfiguration;
import com.elastic.search.elasticsearch.infrastructure.consts.FrameworkExceptionConstants;
import com.elastic.search.elasticsearch.infrastructure.executor.SearchExecutor;
import com.elastic.search.elasticsearch.searchservice.ESSearchService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 * @author niuzhiwei
 */
@Slf4j
public abstract class BaseSearchDeleteExecutor<T> extends SearchAdapter<DeleteESObject> implements SearchExecutor {

    /**
     * 根据主键删除一条数据
     *
     * @param primaryKey
     * @return
     */
    public synchronized Boolean execute(Long primaryKey) throws FrameworkException {
        try {
            DeleteESObject obj = this.setConfig(this.getConfig(), DeleteESObject.class.newInstance());
            obj.setId(primaryKey);
            SearchBaseResult<Boolean> result = SearchBeanContext.getBean(ESSearchService.class).esDelete(obj);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据主键删除一条数据): primaryKey={" + primaryKey + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 根据主键批量删除
     *
     * @param primaryKeys
     * @return
     * @throws FrameworkExceptionConstants
     */
    public synchronized Boolean execute(Long[] primaryKeys) throws FrameworkException {
        try {
            BatchDeleteESObject bdo = new BatchDeleteESObject();
            List<DeleteESObject> list = new ArrayList<DeleteESObject>();
            for (Long key : primaryKeys) {
                DeleteESObject de = this.setConfig(this.getConfig(), DeleteESObject.class.newInstance());
                de.setId(key);
                list.add(de);
            }
            bdo.setDeleteDatas(list);
            SearchBaseResult<Boolean> result = this.esSearchService.esBatchDelete(bdo);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(据主键批量删除): primaryKeys={" + primaryKeys + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 根据条件删除数据
     *
     * @param searchConditions
     * @return
     * @throws FrameworkExceptionConstants
     */
    public synchronized Boolean execute(List<SearchCondition> searchConditions) throws FrameworkException {
        try {
            ConditionDeleteESObject obj = new ConditionDeleteESObject(searchConditions);
            BaseTypeIndexConfiguration conf = this.getConfig();
            obj.setSystemName(conf.getSystemName());
            obj.setIndexName(conf.getIndexName());
            obj.setTypeName(conf.getTypeName());
            SearchBaseResult<Boolean> result = this.esSearchService.conditionDelete(obj);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据条件删除数据): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }
}

