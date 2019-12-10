package com.elastic.search.elasticsearch.infrastructure.executor.impl;


import com.elastic.search.common.boot.SearchBeanContext;
import com.elastic.search.common.exception.FrameworkException;
import com.elastic.search.elasticsearch.infrastructure.bean.Field;
import com.elastic.search.elasticsearch.infrastructure.conf.BaseTypeIndexConfiguration;
import com.elastic.search.elasticsearch.infrastructure.consts.FrameworkExceptionConstants;
import com.elastic.search.elasticsearch.search.utils.ModifyIndexFactory;
import org.elasticsearch.search.sort.SortOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ES索引类型字段变更
 *
 * @author niuzhiwei
 */
public abstract class BaseSearchTypeIndexModifyExecutor {

    /**
     * 获取索引胚子
     *
     * @return
     */
    public abstract BaseTypeIndexConfiguration getConfig();


    public synchronized Boolean execute(List<Field> fields) {
        if (null == fields || fields.size() == 0) {
            return false;
        }
        BaseTypeIndexConfiguration conf = this.getConfig();
        try {
            boolean result = SearchBeanContext.getBean(ModifyIndexFactory.class).reindex(conf.getIndexName(), conf.getTypeName(), properties -> {
                fields.forEach(field -> {
                    if (field.isValid()) {
                        Map<String, Object> f = (Map<String, Object>) properties.get(field.getOriginName());
                        if (null == f) {//新增字段
                            f = new HashMap<>();
                            f.put("type", field.getFieldType().getType());
                            properties.put(field.getCurrentName(), f);
                        } else if (!field.getCurrentName().equals(field.getOriginName())) {//字段名与类型变更
                            f = new HashMap<>();
                            f.put("type", field.getFieldType().getType());
                            properties.put(field.getCurrentName(), f);
                            properties.remove(field.getOriginName());
                        } else {//变更字段类型
                            f.put("type", field.getFieldType().getType());
                        }
                    }
                });
                return properties;
            }, "id", SortOrder.ASC);
            return result;
        } catch (Exception e) {
            throw new FrameworkException(FrameworkExceptionConstants.ERROR_SEARCH_ENGINES, "搜索引擎字段变更异常", e);
        }
    }
}
