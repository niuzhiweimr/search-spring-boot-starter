package com.elastic.search.elasticsearch.search.index;

public enum Option {
    // 分词字段默认是position，其他的默认是docs
    // 索引文档号
    docs,
    // 文档号+词频
    freqs,
    // 文档号+词频+位置，通常用来距离查询
    positions,
    // 文档号+词频+位置+偏移量，通常被使用在高亮字段
    offsets
}

