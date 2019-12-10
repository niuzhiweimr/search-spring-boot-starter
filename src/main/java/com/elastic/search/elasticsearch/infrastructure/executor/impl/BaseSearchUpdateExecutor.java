package com.elastic.search.elasticsearch.infrastructure.executor.impl;


import com.elastic.search.common.boot.SearchBeanContext;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.exception.SystemException;
import com.elastic.search.common.utils.ListUtils;
import com.elastic.search.elasticsearch.convert.ESSearchConvertor;
import com.elastic.search.elasticsearch.dataobject.BatchUpdateESObject;
import com.elastic.search.elasticsearch.dataobject.ConditionUpdateESObject;
import com.elastic.search.elasticsearch.dataobject.UpdateESObject;
import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;
import com.elastic.search.elasticsearch.infrastructure.common.SearchAdapter;
import com.elastic.search.elasticsearch.infrastructure.conf.BaseTypeIndexConfiguration;
import com.elastic.search.elasticsearch.infrastructure.consts.FrameworkExceptionConstants;
import com.elastic.search.elasticsearch.infrastructure.executor.SearchExecutor;
import com.elastic.search.elasticsearch.searchservice.ESSearchService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * es update
 *
 * @param <T>
 * @author niuzhiwei
 */
public abstract class BaseSearchUpdateExecutor<T> extends SearchAdapter<UpdateESObject> implements SearchExecutor {


    /**
     * 根据主键id更新一条数据，支持局部更新，id非空<br/>
     * 局部更新时非更新字段传入null值或剔除
     *
     * @param t
     * @return
     * @throws SystemException
     */
    public synchronized Boolean execute(T t) throws SystemException {
        try {
            UpdateESObject obj = this.setConfig(this.getConfig(), UpdateESObject.class.newInstance());
            Field f = t.getClass().getDeclaredField(primaryKeyName);
            f.setAccessible(true);
            obj.setId(f.get(t));
            Map<Object, Object> data = ESSearchConvertor.object2MapExcludeNullValue(t);
            List<Node> nodes = this.getCascade(t);
            if (ListUtils.isNotBlank(nodes)) {
                nodes.forEach(node -> {
                    data.put(String.format("_%s", node.getNodeName()), node.getNodeValue());
                });
            }
            obj.setDataMap(data);
            SearchBaseResult<Boolean> result = SearchBeanContext.getBean(ESSearchService.class).esUpdate(obj);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据主键id更新一条数据): t={" + t + "}, error={" + e.getMessage() + "}");
        }
    }

    /**
     * 根据主键id更新一条数据，支持局部更新，id非空<br/>
     * eliminate：是否剔除NULL值属性
     *
     * @param t
     * @return
     * @throws SystemException
     */
    public synchronized Boolean execute(T t, Boolean eliminate) throws SystemException {
        try {
            if (eliminate) {
                return this.execute(t);
            }
            UpdateESObject obj = this.setConfig(this.getConfig(), UpdateESObject.class.newInstance());
            Field f = t.getClass().getDeclaredField(primaryKeyName);
            f.setAccessible(true);
            obj.setId(f.get(t));
            Map<Object, Object> data = ESSearchConvertor.object2Map(t);
            List<Node> nodes = this.getCascade(t);
            if (ListUtils.isNotBlank(nodes)) {
                nodes.forEach(node -> {
                    data.put(String.format("_%s", node.getNodeName()), node.getNodeValue());
                });
            }
            obj.setDataMap(data);
            SearchBaseResult<Boolean> result = SearchBeanContext.getBean(ESSearchService.class).esUpdate(obj);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据主键id更新一条数据): t={" + t + "}, error={" + e.getMessage() + "}");
        }
    }


    /**
     * 多条件更新数据
     *
     * @return
     * @throws SystemException
     */
    public synchronized Boolean execute(T t, List<SearchCondition> searchConditions) throws SystemException {
        try {
            ConditionUpdateESObject obj = ConditionUpdateESObject.class.newInstance();
            BaseTypeIndexConfiguration conf = this.getConfig();
            obj.setSystemName(conf.getSystemName());
            obj.setIndexName(conf.getIndexName());
            obj.setTypeName(conf.getTypeName());
            obj.setConditions(searchConditions);
            Map<Object, Object> data = ESSearchConvertor.object2MapExcludeNullValue(t);
            List<Node> nodes = this.getCascade(t);
            if (ListUtils.isNotBlank(nodes)) {
                nodes.forEach(node -> {
                    data.put(String.format("_%s", node.getNodeName()), node.getNodeValue());
                });
            }
            obj.setDataMap(data);
            SearchBaseResult<Boolean> result = SearchBeanContext.getBean(ESSearchService.class).conditionUpdate(obj);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                throw new Exception(result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(多条件更新数据): t={" + t + "}, error={" + e.getMessage() + "}");
        }
    }

    /**
     * 根据主键批量更新
     *
     * @param list
     * @return
     */
    public synchronized Boolean execute(List<T> list) throws SystemException {
        try {
            BatchUpdateESObject obj = BatchUpdateESObject.class.newInstance();
            List<UpdateESObject> objs = new ArrayList<UpdateESObject>();
            Field f = null;
            for (T t : list) {
                UpdateESObject o = this.setConfig(this.getConfig(), UpdateESObject.class.newInstance());
                o.setDataMap(ESSearchConvertor.object2MapExcludeNullValue(t));
                f = t.getClass().getDeclaredField(primaryKeyName);
                f.setAccessible(true);
                o.setId(f.get(t));
                Map<Object, Object> data = ESSearchConvertor.object2MapExcludeNullValue(t);
                List<Node> nodes = this.getCascade(t);
                if (ListUtils.isNotBlank(nodes)) {
                    nodes.forEach(node -> {
                        data.put(String.format("_%s", node.getNodeName()), node.getNodeValue());
                    });
                }
                o.setDataMap(data);
                objs.add(o);
            }
            obj.setUpdateDatas(objs);
            SearchBaseResult<Boolean> result = SearchBeanContext.getBean(ESSearchService.class).esBatchUpdate(obj);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                throw new Exception(result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据主键批量更新): list={" + list + "}, error={" + e.getMessage() + "}");
        }
    }

    /**
     * 多条件更新数据 会将值设置为空
     *
     * @return
     * @throws SystemException
     */
    public synchronized Boolean executeWithNull(T t, List<SearchCondition> searchConditions) throws SystemException {
        try {
            ConditionUpdateESObject obj = ConditionUpdateESObject.class.newInstance();
            BaseTypeIndexConfiguration conf = this.getConfig();
            obj.setSystemName(conf.getSystemName());
            obj.setIndexName(conf.getIndexName());
            obj.setTypeName(conf.getTypeName());
            obj.setConditions(searchConditions);
            Map<Object, Object> data = ESSearchConvertor.object2Map(t);
            List<Node> nodes = this.getCascade(t);
            if (ListUtils.isNotBlank(nodes)) {
                nodes.forEach(node -> {
                    data.put(String.format("_%s", node.getNodeName()), node.getNodeValue());
                });
            }
            obj.setDataMap(data);
            SearchBaseResult<Boolean> result = esSearchService.conditionUpdate(obj);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                throw new Exception(result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(多条件更新数据): t={" + t + "}, error={" + e.getMessage() + "}");
        }
    }

    /**
     * 多条件更新数据
     *
     * @return
     * @throws SystemException
     */
    public synchronized Boolean executeWithNull(T t) throws SystemException {
        try {
            UpdateESObject obj = this.setConfig(this.getConfig(), UpdateESObject.class.newInstance());
            Field f = t.getClass().getDeclaredField(primaryKeyName);
            f.setAccessible(true);
            obj.setId(f.get(t));
            Map<Object, Object> data = ESSearchConvertor.object2Map(t);
            List<Node> nodes = this.getCascade(t);
            if (ListUtils.isNotBlank(nodes)) {
                nodes.forEach(node -> {
                    data.put(String.format("_%s", node.getNodeName()), node.getNodeValue());
                });
            }
            obj.setDataMap(data);
            SearchBaseResult<Boolean> result = SearchBeanContext.getBean(ESSearchService.class).esUpdate(obj);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据主键id更新一条数据): t={" + t + "}, error={" + e.getMessage() + "}");
        }
    }

    /**
     * 根据主键批量更新
     *
     * @param list
     * @return
     */
    public synchronized Boolean executeWithNull(List<T> list) throws SystemException {
        try {
            BatchUpdateESObject obj = BatchUpdateESObject.class.newInstance();
            List<UpdateESObject> objs = new ArrayList<UpdateESObject>();
            Field f = null;
            for (T t : list) {
                UpdateESObject o = this.setConfig(this.getConfig(), UpdateESObject.class.newInstance());
                o.setDataMap(ESSearchConvertor.object2Map(t));
                f = t.getClass().getDeclaredField(primaryKeyName);
                f.setAccessible(true);
                o.setId(f.get(t));
                Map<Object, Object> data = ESSearchConvertor.object2Map(t);
                List<Node> nodes = this.getCascade(t);
                if (ListUtils.isNotBlank(nodes)) {
                    nodes.forEach(node -> {
                        data.put(String.format("_%s", node.getNodeName()), node.getNodeValue());
                    });
                }
                o.setDataMap(data);
                objs.add(o);
            }
            obj.setUpdateDatas(objs);
            SearchBaseResult<Boolean> result = SearchBeanContext.getBean(ESSearchService.class).esBatchUpdate(obj);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                throw new Exception(result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(根据主键批量更新): list={" + list + "}, error={" + e.getMessage() + "}");
        }
    }

}
