package com.elastic.search.elasticsearch.search.index;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 索引解析器
 *
 * @author jack
 */
public class IndexParser {
    String index;
    String type;
    private Map<String, JsonObject> properties = new HashMap<>();

    public static IndexStruct parseAndBuildUpdateIndex(String json) {
        IndexParser indexParser = new IndexParser();
        JsonElement element = new JsonParser().parse(json);
        if (element.isJsonObject()) {
            JsonObject indexNode = indexParser.parseIndex(element.getAsJsonObject());
            JsonObject mappingNode = (JsonObject) indexNode.get("mappings");
            if (mappingNode != null) {
                Iterator<Map.Entry<String, JsonElement>> iterator = mappingNode.entrySet().iterator();
                if (iterator.hasNext()) {
                    Map.Entry<String, JsonElement> entry = iterator.next();
                    indexParser.type = entry.getKey();
                    JsonObject propsNode = (JsonObject) entry.getValue();
                    JsonObject fieldsObject = (JsonObject) propsNode.get("properties");
                    if (fieldsObject != null) {
                        indexParser.parse(fieldsObject.entrySet().iterator());
                        return indexParser.buildUpdateIndex();
                    }
                }
            }
        }
        System.err.println("无效的JSON:" + json);
        return null;
        // throw new IllegalArgumentException("无效的Json:" + json);
    }

    public static IndexParser parse(String json) {
        IndexParser indexParser = new IndexParser();
        JsonElement element = new JsonParser().parse(json);
        if (element.isJsonObject()) {
            JsonObject indexNode = indexParser.parseIndex(element.getAsJsonObject());
            JsonObject mappingNode = (JsonObject) indexNode.get("mappings");
            if (mappingNode != null) {
                Iterator<Map.Entry<String, JsonElement>> iterator = mappingNode.entrySet().iterator();
                if (iterator.hasNext()) {
                    Map.Entry<String, JsonElement> entry = iterator.next();
                    indexParser.type = entry.getKey();
                    JsonObject propsNode = (JsonObject) entry.getValue();
                    JsonObject fieldsObject = (JsonObject) propsNode.get("properties");
                    if (fieldsObject != null) {
                        indexParser.parse(fieldsObject.entrySet().iterator());
                    }
                }
            }
        }
        return indexParser;
    }

    /**
     * 从索引类型解析
     *
     * @param json
     * @param indexType
     * @return
     */
    public static IndexParser parseFromIndexType(String json, String indexType) {
        IndexParser indexParser = new IndexParser();
        JsonObject element = new JsonParser().parse(json).getAsJsonObject();
        element = element.get(indexType).getAsJsonObject();
        JsonObject fieldsObject = element.get("properties").getAsJsonObject();
        indexParser.parse(fieldsObject.entrySet().iterator());
        return indexParser;
    }

    private JsonObject parseIndex(JsonObject jsonObject) {
        Map.Entry<String, JsonElement> entry = jsonObject.entrySet().iterator().next();
        this.index = entry.getKey();
        return entry.getValue().getAsJsonObject();
    }

    private void parse(Iterator<Map.Entry<String, JsonElement>> iterator) {
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            properties.put(entry.getKey(), (JsonObject) entry.getValue());
        }
    }

    private IndexStruct buildUpdateIndex() {
        IndexStruct struct = IndexStruct.make(this.index, this.type);
        this.properties.forEach((key, jsonObject) -> {
            if (jsonObject.has("type")) {
                if (jsonObject.get("type").getAsString().equals(DataType.TEXT.name().toLowerCase())) {
                    if (jsonObject.has("fields")) {
                        JsonObject field = jsonObject.get("fields").getAsJsonObject();
                        if (field.has("keyword") && field.get("keyword").getAsJsonObject().has("type")) {
                            return;
                        }
                    }
                    // 添加需要增加fields->keyword
                    struct.addColumn(key, IndexField.make().setType(DataType.TEXT).setFieldKeyword());
                }
            }
        });
        return struct;
    }

    public Map<String, JsonObject> getProperties() {
        return properties;
    }
}

