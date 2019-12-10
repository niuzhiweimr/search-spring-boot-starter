package com.elastic.search.elasticsearch.search.index;

import java.util.HashMap;
import java.util.Map;

public class Field {
    Map<String, Object> keyword = new HashMap<>();

    public static Field make() {
        return new Field();
    }

    public Field setType(DataType type) {
        keyword.put("type", type.name().toLowerCase());
        return this;
    }

    /**
     * 忽略字符长度ignore_above以外的字符，不被索引
     *
     * @param ignore_above
     * @return
     */
    public Field setIgnoreAbove(int ignore_above) {
        keyword.put("ignore_above", ignore_above);
        return this;
    }

    /**
     * 超过256个字符的文本，将会被忽略，不被索引
     *
     * @return
     */
    public Field setIgnoreAbove() {
        keyword.put("ignore_above", 256);
        return this;
    }
}
