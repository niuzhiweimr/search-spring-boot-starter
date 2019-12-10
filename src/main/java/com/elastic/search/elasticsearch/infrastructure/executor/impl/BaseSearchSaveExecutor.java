package com.elastic.search.elasticsearch.infrastructure.executor.impl;


import com.elastic.search.common.boot.SearchBeanContext;
import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.common.exception.SystemException;
import com.elastic.search.common.utils.ListUtils;
import com.elastic.search.elasticsearch.convert.ESSearchConvertor;
import com.elastic.search.elasticsearch.infrastructure.common.SearchAdapter;
import com.elastic.search.elasticsearch.infrastructure.conf.BaseTypeIndexConfiguration;
import com.elastic.search.elasticsearch.infrastructure.consts.FrameworkExceptionConstants;
import com.elastic.search.elasticsearch.dataobject.BatchSaveESObject;
import com.elastic.search.elasticsearch.dataobject.SaveESObject;
import com.elastic.search.elasticsearch.infrastructure.executor.SearchExecutor;
import com.elastic.search.elasticsearch.searchservice.ESSearchService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @author niuzhiwei
 */
@Slf4j
public abstract class BaseSearchSaveExecutor<T> extends SearchAdapter<SaveESObject> implements SearchExecutor {

    /**
     * 插入一条数据，支持嵌套插入
     *
     * @param t
     * @return
     * @throws SystemException
     */
    public synchronized Boolean execute(T t) throws SystemException {
        try {
            BaseTypeIndexConfiguration config = this.getConfig();
            SaveESObject obj = this.setConfig(config, new SaveESObject());
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
            SearchBaseResult<Boolean> result = SearchBeanContext.getBean(ESSearchService.class).esSave(obj);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(插入一条数据): t={" + t + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }

    /**
     * 批量插入
     *
     * @param list
     * @return
     * @throws SystemException
     */
    public synchronized boolean execute(List<T> list) throws SystemException {
        try {
            BatchSaveESObject bso = new BatchSaveESObject();
            List<SaveESObject> so = new ArrayList<SaveESObject>();
            Field f = null;
            for (T t : list) {
                SaveESObject obj = this.setConfig(this.getConfig(), SaveESObject.class.newInstance());
                f = t.getClass().getDeclaredField(primaryKeyName);
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
                so.add(obj);
            }
            bso.setSaveDatas(so);
            SearchBaseResult<Boolean> result = esSearchService.esBatchSave(bso);
            if (result.isSuccess()) {
                return result.getResult();
            } else {
                throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, result.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎异常(批量插入): list={" + list + "}, error={" + e.getLocalizedMessage() + "}");
        }
    }
}
