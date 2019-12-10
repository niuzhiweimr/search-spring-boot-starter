package com.elastic.search.elasticsearch.search.index;

public enum TermVector {
    // 默认不存储向量信息，
    no,
    // 支持term存储
    yes,
    // term+位置
    with_positions,
    // （term+偏移量）
    with_offsets,
    // term+位置+偏移量 对快速高亮fast vector
    with_positions_offsets,
    // highlighter能提升性能，但开启又会加大索引体积，不适合大数据量用

}

