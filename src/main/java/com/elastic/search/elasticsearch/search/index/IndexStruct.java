package com.elastic.search.elasticsearch.search.index;


import com.elastic.search.elasticsearch.serialize.api.json.GsonSerialize;

import java.util.HashMap;
import java.util.Map;


/**
 * @author niuzhiwei
 */
public class IndexStruct {

    public Map<String, IndexType> mappings = new HashMap<>();
    private transient IndexType indexType;
    private transient String type;
    private transient String index;

    public String getType() {
        return type;
    }

    public String getIndex() {
        return index;
    }

    /**
     * 创建索引结构体
     *
     * @param type 索引类型名称
     * @return
     */
    public static IndexStruct make(String index, String type) {
        return new IndexStruct().init(index, type);
    }

    private IndexStruct init(String index, String type) {
        this.indexType = new IndexType();
        this.index = index;
        this.type = type;
        this.mappings.put(type, indexType);
        return this;
    }

    public IndexStruct addColumn(String name, IndexField column) {
        this.indexType.properties.put(name, column.getResult());
        return this;
    }

    public String getIndexJson() {
        return GsonSerialize.INSTANCE.encode(this);
    }

    public Map<String, Object> getIndexAsMap() {
        return GsonSerialize.INSTANCE.decode(getIndexJson(), Map.class);
    }

    public static class IndexType {
        public Map<String, Map<String, Object>> properties = new HashMap<>();
    }

    public Map<String, Map<String, Object>> getProperties() {
        return indexType.properties;
    }

    public String getPropertiesJson() {
        return GsonSerialize.INSTANCE.encode(indexType);
    }
}

