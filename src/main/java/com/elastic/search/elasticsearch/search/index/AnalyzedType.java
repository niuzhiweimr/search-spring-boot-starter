package com.elastic.search.elasticsearch.search.index;

public enum AnalyzedType {

    // 分词
    ANALYZED,
    // 不分词
    NOT_ANALYZED,
    // 设置成no，字段将不会被索引
    NO;
}

