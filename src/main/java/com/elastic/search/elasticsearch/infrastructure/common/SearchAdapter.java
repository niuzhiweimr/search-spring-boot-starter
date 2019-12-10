package com.elastic.search.elasticsearch.infrastructure.common;

import com.elastic.search.elasticsearch.infrastructure.annotation.Cascade;
import com.elastic.search.elasticsearch.infrastructure.annotation.Cascades;
import com.elastic.search.elasticsearch.infrastructure.annotation.EsObject;
import com.elastic.search.elasticsearch.infrastructure.conf.BaseTypeIndexConfiguration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.elastic.search.elasticsearch.dataobject.BaseESObject;
import com.elastic.search.elasticsearch.searchservice.ESSearchService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author niuzhiwei
 */
public class SearchAdapter<K> {

    /**
     * 缓存
     */
    private static final Map<Class, List<String>> NEED_FIELDS_CACHE = Maps.newHashMap();
    /**
     * 搜索服务接口，封装类无状态
     */
    protected ESSearchService esSearchService;
    /**
     * 主键hash，搜索接口需要
     */
    protected Map<Object, Object> primary = new HashMap<Object, Object>();
    /**
     * 默认主键名称，作为搜索主键
     */
    protected final String primaryKeyName = "id";

    @SuppressWarnings("unchecked")
    protected K setConfig(BaseTypeIndexConfiguration conf, K k) {
        BaseESObject obj = (BaseESObject) k;
        obj.setSystemName(conf.getSystemName());
        obj.setIndexName(conf.getIndexName());
        obj.setTypeName(conf.getTypeName());
        return (K) obj;
    }

    @SuppressWarnings("unchecked")
    protected <T> Class<T> getGenericActualType(Class<?> clazz) {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<T> c = (Class<T>) (parameterizedType.getActualTypeArguments()[0]);
        return c;
    }

    protected <T> List<String> getNeedFields(Class<?> clazz) {
        List<String> fieldNames = NEED_FIELDS_CACHE.get(clazz);
        if (null == fieldNames) {
            fieldNames = Lists.newArrayList();
            Field[] fs = FieldUtils.getAllFields(clazz);
            for (Field f : fs) {
                if ("serialVersionUID".equals(f.getName())) {
                    continue;
                }
                if (null == f.getAnnotation(EsObject.class)) {
                    fieldNames.add(f.getName());
                } else if (f.getType().isAssignableFrom(List.class)) {
                    String prefix = f.getName() + ".";
                    ParameterizedType pt = (ParameterizedType) f.getGenericType();
                    if (ArrayUtils.isEmpty(pt.getActualTypeArguments())) {
                        continue;
                    }
                    for (String fieldName : getNeedFields((Class) pt.getActualTypeArguments()[0])) {
                        fieldNames.add(prefix + fieldName);
                    }
                } else if (f.getType().isArray()) {
                    String prefix = f.getName() + ".";
                    for (String fieldName : getNeedFields(f.getType().getComponentType())) {
                        fieldNames.add(prefix + fieldName);
                    }

                } else {
                    String prefix = f.getName() + ".";
                    for (String fieldName : getNeedFields(f.getType())) {
                        fieldNames.add(prefix + fieldName);
                    }
                }

            }
            NEED_FIELDS_CACHE.put(clazz, fieldNames);
        }
        return fieldNames;
    }

    protected <T> List<Node> getCascade(T t) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Cascade cascade = t.getClass().getDeclaredAnnotation(Cascade.class);
        Cascades cascades = t.getClass().getDeclaredAnnotation(Cascades.class);
        List<Node> fields = new ArrayList<>();

        //单个关联
        Node f = null;
        if (null != cascade && null != (f = this.getNode(t, cascade))) {
            fields.add(f);
        }
        //多个关联
        Cascade[] cs = null;
        if (null != cascades && (cs = cascades.value()).length != 0) {
            for (Cascade c : cs) {
                Node f2 = null;
                if (null != cascade && null != (f2 = this.getNode(t, c))) {
                    fields.add(f2);
                }
            }
        }
        return fields;
    }

    private <T> Node getNode(T t, Cascade cascade) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        String[] fieldNames = cascade.fields();

        if (StringUtils.isBlank(cascade.value()) || fieldNames.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        Field field = null;
        Object val = null;

        for (String fieldName : fieldNames) {
            field = t.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            val = field.get(t);
            if (null != val) {
                sb.append(val).append("\t");
            }
        }
        return new Node(cascade.value(), sb.toString());
    }

    protected class Node {
        private String nodeName;
        private String nodeValue;

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

        public String getNodeValue() {
            return nodeValue;
        }

        public void setNodeValue(String nodeValue) {
            this.nodeValue = nodeValue;
        }

        public Node(String nodeName, String nodeValue) {
            this.nodeName = nodeName;
            this.nodeValue = nodeValue;
        }
    }
}
