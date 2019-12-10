package com.elastic.search.elasticsearch.infrastructure.executor.impl;

import com.elastic.search.common.boot.SearchBeanContext;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.exception.FrameworkException;
import com.elastic.search.common.exception.SystemException;
import com.elastic.search.elasticsearch.infrastructure.common.SearchAdapter;
import com.elastic.search.elasticsearch.infrastructure.consts.FrameworkExceptionConstants;
import com.elastic.search.elasticsearch.dataobject.QueryESObject;
import com.elastic.search.elasticsearch.dataobject.StatisticESObject;
import com.elastic.search.elasticsearch.dataobject.conditions.FunctionCondition;
import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;
import com.elastic.search.elasticsearch.dataobject.enums.SqlFunctionEnum;
import com.elastic.search.elasticsearch.infrastructure.executor.SearchExecutor;
import com.elastic.search.elasticsearch.searchservice.ESStatisticService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author niuzhiwei
 */
@Slf4j
public abstract class BaseSearchAggregateExecutor<T> extends SearchAdapter<QueryESObject> implements SearchExecutor {

    private final static String AGGREGATE_COUNT = "count";
    private final static String AGGREGATE_SUM = "sum";
    private final static String AGGREGATE_MAX = "max";
    private final static String AGGREGATE_MIN = "min";
    private final static String AGGREGATE_AVG = "avg";

    /**
     * (聚合)根据条件获得列表总数
     *
     * @param searchConditions
     * @return
     * @throws SystemException
     */
    public synchronized Integer count(List<SearchCondition> searchConditions) throws SystemException {
        try {
            StatisticESObject obj = getObj();
            obj.setSearchConditions(searchConditions);
            obj.setFunctionConditions(getFunctionConditions(this.primaryKeyName, AGGREGATE_COUNT, SqlFunctionEnum.COUNT));
            SearchBaseResult<Map<String, Number>> result = SearchBeanContext.getBean(ESStatisticService.class).statisticByConditions(obj);
            if (result.isSuccess()) {
                return result.getResult().get(AGGREGATE_COUNT).intValue();
            } else {
                throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据条件获得列表总数): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    private synchronized List<FunctionCondition> getFunctionConditions(String fieldName, String functionName, SqlFunctionEnum type) {
        FunctionCondition func = new FunctionCondition();
        func.setField(fieldName);
        func.setFunction(type);
        func.setFunctionName(functionName);
        List<FunctionCondition> funcs = new ArrayList<>();
        funcs.add(func);
        return funcs;
    }

    private synchronized StatisticESObject getObj() throws InstantiationException, IllegalAccessException {
        StatisticESObject obj = StatisticESObject.class.newInstance();
        obj.setSystemName(this.getConfig().getSystemName());
        obj.setIndexName(this.getConfig().getIndexName());
        obj.setTypeName(this.getConfig().getTypeName());
        return obj;
    }

    /**
     * (聚合)根据条件获得列最大值
     *
     * @param searchConditions
     * @return
     * @throws SystemException
     */
    public synchronized Double max(String fieldName, List<SearchCondition> searchConditions) throws SystemException {
        try {
            StatisticESObject obj = getObj();
            obj.setSearchConditions(searchConditions);
            obj.setFunctionConditions(getFunctionConditions(fieldName, AGGREGATE_MAX, SqlFunctionEnum.MAX));
            SearchBaseResult<Map<String, Number>> result = SearchBeanContext.getBean(ESStatisticService.class).statisticByConditions(obj);
            if (result.isSuccess()) {
                return result.getResult().get(AGGREGATE_MAX).doubleValue();
            } else {
                throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据条件获得列最大值): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * (聚合)根据条件获得列最小值
     *
     * @param searchConditions
     * @return
     * @throws SystemException
     */
    public synchronized Double min(String fieldName, List<SearchCondition> searchConditions) throws SystemException {
        try {
            StatisticESObject obj = getObj();
            obj.setSearchConditions(searchConditions);
            obj.setFunctionConditions(getFunctionConditions(fieldName, AGGREGATE_MIN, SqlFunctionEnum.MIN));
            SearchBaseResult<Map<String, Number>> result = SearchBeanContext.getBean(ESStatisticService.class).statisticByConditions(obj);
            if (result.isSuccess()) {
                return result.getResult().get(AGGREGATE_MIN).doubleValue();
            } else {
                throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据条件获得列最小值): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * (聚合)根据条件获得列平均值
     *
     * @param searchConditions
     * @return
     * @throws SystemException
     */
    public synchronized Double avg(String fieldName, List<SearchCondition> searchConditions) throws SystemException {
        try {
            StatisticESObject obj = getObj();
            obj.setSearchConditions(searchConditions);
            obj.setFunctionConditions(getFunctionConditions(fieldName, AGGREGATE_AVG, SqlFunctionEnum.AVG));
            SearchBaseResult<Map<String, Number>> result = SearchBeanContext.getBean(ESStatisticService.class).statisticByConditions(obj);
            if (result.isSuccess()) {
                return result.getResult().get(AGGREGATE_AVG).doubleValue();
            } else {
                throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据条件获得列平均值): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * (聚合)根据条件获得列总和
     *
     * @param searchConditions
     * @return
     * @throws SystemException
     */
    public synchronized Double sum(String fieldName, List<SearchCondition> searchConditions) throws SystemException {
        try {
            StatisticESObject obj = getObj();
            obj.setSearchConditions(searchConditions);
            obj.setFunctionConditions(getFunctionConditions(fieldName, AGGREGATE_SUM, SqlFunctionEnum.SUM));
            SearchBaseResult<Map<String, Number>> result = SearchBeanContext.getBean(ESStatisticService.class).statisticByConditions(obj);
            if (result.isSuccess()) {
                return result.getResult().get(AGGREGATE_SUM).doubleValue();
            } else {
                throw new Exception(result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据条件获得列总和): searchConditions={" + searchConditions + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }
}
