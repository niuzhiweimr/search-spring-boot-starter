package com.elastic.search.elasticsearch.search.index;


import com.elastic.search.elasticsearch.serialize.api.json.GsonSerialize;

import java.util.HashMap;
import java.util.Map;

/**
 * http://blog.csdn.net/qq_24400639/article/details/51602646
 *
 * @author jack
 */
public class IndexField {

    Map<String, Object> map = new HashMap<>();

    public static IndexField make() {
        return new IndexField();
    }

    /**
     * 设置数据类型
     *
     * @param type
     * @return
     */
    public IndexField setType(DataType type) {
        map.put("type", type.name().toLowerCase());
        return this;
    }

    /**
     * 设置字段级别的分数加权
     *
     * @param boost
     * @return
     */
    public IndexField setBoost(float boost) {
        map.put("boost", boost);
        return this;
    }

    /**
     * 设置中文IK分词器
     *
     * @return
     */
    public IndexField setIKAnalyzer() {
        map.put("analyzer", "ik_max_word");
        return this;
    }

    /**
     * 设置搜索分词
     *
     * @param analyzer
     * @return
     */
    public IndexField setSearchAnalyzer(String analyzer) {
        map.put("search_analyzer", analyzer);
        return this;
    }

    /**
     * 设置分词器
     *
     * @param analyzer
     * @return
     */
    public IndexField setAnalyzer(String analyzer) {
        map.put("analyzer", analyzer);
        return this;
    }

    /**
     * 设置索引方式
     *
     * @param indexType
     * @return
     */
    public IndexField setIndex(AnalyzedType indexType) {
        map.put("index", indexType.name().toLowerCase());
        return this;
    }

    /**
     * 设置不分词
     *
     * @return
     */
    public IndexField setNotAnalyzer() {
        map.put("not_analyzed", "not_analyzed");
        return this;
    }

    /**
     * 设置是否此字段包含在_all字段中，默认是true，除非index设置成no选项
     *
     * @return
     */
    public IndexField setIncludeInAll(boolean include) {
        map.put("include_in_all", include);
        return this;
    }

    /**
     * 可以对一个字段提供多种索引模式，同一个字段的值，一个分词，一个不分词
     * "fields":{"raw":{"type":"string","index":"not_analyzed"}}
     *
     * @param value
     * @return
     */
    public IndexField setField(Field value) {
        map.put("fields", value);
        return this;
    }

    /**
     * 在分词到字段上支持keyword
     *
     * @return
     */
    public IndexField setFieldKeyword() {
        map.put("fields", Field.make().setType(DataType.KEYWORD));

        return this;
    }

    /**
     * 获取字段结果
     *
     * @return
     */
    public Map<String, Object> getResult() {
        return map;
    }

    public String getResultAsJson() {
        return GsonSerialize.INSTANCE.encode(getResult());
    }

    /**
     * type:date 日期类型使用格式化 strict_date_optional_time||epoch_millis（默认）
     *
     * @param format
     * @return
     */
    public IndexField setFormat(String format) {
        // yyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis
        this.map.put("format", format);
        return this;
    }

    /**
     * 是否单独设置此字段的是否存储而从_source字段中分离，默认是false，只能搜索，不能获取值
     *
     * @param store
     * @return
     */
    // store存储
    // true 独立存储
    // false（默认）不存储，从_source中解析
    public IndexField setStore(boolean store) {
        this.map.put("store", store);
        return this;
    }

    /**
     * 对not_analyzed字段，默认都是开启，分词字段不能使用，对排序和聚合能提升较大性能，节约内存
     *
     * @param doc_values
     * @return
     */
    public IndexField setDocValues(boolean doc_values) {
        this.map.put("doc_values", doc_values);
        return this;
    }

    /**
     * 忽略字符长度ignore_above以外的字符，不被索引
     *
     * @param ignore_above
     * @return
     */
    public IndexField setIgnoreAbove(int ignore_above) {
        map.put("ignore_above", ignore_above);
        return this;
    }

    // 超过256个字符的文本，将会被忽略，不被索引
    public IndexField setIgnoreAbove() {
        map.put("ignore_above", 256);
        return this;
    }

    /**
     * "fielddata":{"format":"disabled"}
     * //针对分词字段，参与排序或聚合时能提高性能，不分词字段统一建议使用doc_value
     *
     * @return
     */
    public IndexField setFieldData() {
        Map<String, Object> data = new HashMap<>();
        data.put("format", "disabled");
        map.put("fielddata", data);
        return this;
    }

    public IndexField setIndexOptions(Option option) {
        this.map.put("index_options", option.name());
        return this;
    }

    /**
     * 存储长度因子和索引时boost，建议对需要参与评分字段使用，会额外增加内存消耗量
     *
     * @param enable
     * @return
     */
    public IndexField setNorms(boolean enable) {
        // 分词字段默认配置 {"enable":true,"loading":"lazy"}
        // 不分词字段：默认{"enable":false}
        Map<String, Object> data = new HashMap<>();
        data.put("enable", enable);
        if (enable) {
            data.put("loading", "lazy");
        }
        return this;
    }

    /**
     * 设置一些缺失字段的初始化值，只有string可以使用，分词字段的null值也会被分词
     *
     * @return
     */
    public IndexField setNullValue() {
        this.map.put("null_value", "NULL");
        return this;
    }

    /**
     * 设置一些缺失字段的初始化值，只有string可以使用，分词字段的null值也会被分词
     *
     * @return
     */
    public IndexField setNullValue(String defaultValue) {
        this.map.put("null_value", defaultValue);
        return this;
    }

    /**
     * "position_increament_gap":0
     * 影响距离查询或近似查询，可以设置在多值字段的数据上或分词字段上，查询时可指定slop间隔，默认值是100
     *
     * @return
     */
    public IndexField setPositionIncreamentGap(int position_increament_gap) {
        this.map.put("position_increament_gap", position_increament_gap);
        return this;
    }

    /**
     * "similarity":"BM25"//默认是TF/IDF算法，指定一个字段评分策略，仅仅对字符串型和分词类型有效
     *
     * @param value
     * @return
     */
    public IndexField setSimilarity(String value) {
        this.map.put("similarity", value);
        return this;
    }

    /**
     * "term_vector":"no"//默认不存储向量信息，支持参数yes（term存储），
     * with_positions（term+位置）,with_offsets（term+偏移量），
     * with_positions_offsets(term+位置+偏移量) 对快速高亮fast vector
     * highlighter能提升性能，但开启又会加大索引体积，不适合大数据量用
     *
     * @param term_vector
     * @return
     */
    public IndexField setTermVector(TermVector term_vector) {
        this.map.put("term_vector", term_vector.name());
        return this;
    }

    public IndexField put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
