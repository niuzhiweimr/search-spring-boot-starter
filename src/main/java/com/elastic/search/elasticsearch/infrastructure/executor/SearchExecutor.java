package com.elastic.search.elasticsearch.infrastructure.executor;


import com.elastic.search.elasticsearch.infrastructure.conf.BaseTypeIndexConfiguration;

/**
 * @author niuzhiwei
 */
public interface SearchExecutor {

    /**
     * 获取索引配置
     *
     * @return
     */
    BaseTypeIndexConfiguration getConfig();
}
