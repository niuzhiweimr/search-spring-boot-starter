package com.elastic.search.elasticsearch.convert;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * es通用对象转换工具
 *
 * @author niuzhiwei
 */
public final class ESSearchConvertor {

    /**
     * 以此判断当前实例对象是否为java的基本类型数据
     */
    private static final String BASIC_DATA_TYPE = "java.";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将实例对象转换为Map
     *
     * @throws RuntimeException illegal json data
     */
    @SuppressWarnings(value = {"unchecked"})
    public static Map<Object, Object> object2Map(Object obj) throws IOException {
        return illegal(obj) ? null : OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(obj), Map.class);
    }

    /**
     * 将实例对象转换为Map，并排除空值
     *
     * @throws RuntimeException illegal json data
     */
    @SuppressWarnings(value = {"unchecked"})
    public static Map<Object, Object> object2MapExcludeNullValue(Object obj) throws IOException {
        if (obj == null) {
            return null;
        }
        final Map<Object, Object> map = OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(obj), Map.class);
        removeNuLLValue(map);
        return map;
    }

    /**
     * 移除 map 中value为null的 entry 对
     *
     * @param map
     */
    private static void removeNuLLValue(Map<Object, Object> map) {
        if (map == null) {
            return;
        }
        for (final Iterator<Map.Entry<Object, Object>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<Object, Object> next = iterator.next();
            final Object value = next.getValue();
            if (value == null) {
                iterator.remove();
            }
            if (value instanceof Map) {
                removeNuLLValue((Map<Object, Object>) value);
            }
        }
    }

    /**
     * 将Map转换为实例对象
     *
     * @throws IOException illegal data
     */
    public static <T> T map2Object(Map<Object, Object> map, Class<T> t) throws IOException {
        return map == null ? null : OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(map), t);
    }

    /**
     * 将实例对象序列化为Json串
     *
     * @throws IOException illegal data
     */
    public static String object2Json(Object obj) throws IOException {
        return obj == null ? null : OBJECT_MAPPER.writeValueAsString(obj);
    }

    /**
     * 将json串转反序列化实例对象
     *
     * @throws IOException illegal data
     */
    public static <T> T json2Object(String jsonStr, Class<T> t) throws IOException {
        if (jsonStr == null || "".equalsIgnoreCase(jsonStr)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(jsonStr, t);
    }

    /**
     * 验证请求参数是否合法
     *
     * @param obj 实例对象
     * @return <code>true</code>obj 为空或为基本类型,<code>false</code>合法数据；
     */
    private static boolean illegal(Object obj) {
        return null == obj || basicDataType(obj);
    }

    /**
     * 验证请求参数是否为基本类型
     *
     * @param obj 实例对象
     * @return <code>true</code>obj 为基本类型,<code>false</code>非基本类型；
     */
    private static boolean basicDataType(Object obj) {
        return obj.getClass().getName().startsWith(BASIC_DATA_TYPE);
    }
}

