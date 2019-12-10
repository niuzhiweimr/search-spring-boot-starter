package com.elastic.search.elasticsearch.searchservice;


import com.elastic.search.common.domain.SearchBaseResult;
import com.elastic.search.elasticsearch.dataobject.CollapseQueryObject;
import com.elastic.search.elasticsearch.dataobject.ESResponse;
import com.elastic.search.elasticsearch.dataobject.StatisticESObject;

import java.util.Map;

/**
 * <p>
 * ES 统计相关服务
 * @author niuzhiwei
 */
public interface ESStatisticService {

    /**
     * 全局统计服务，不受分页效果影响
     *
     * @param esObject ES 统计请求参数
     * @return key:functionName;value:statistic value
     */
    SearchBaseResult<Map<String, Number>> statisticByConditions(StatisticESObject esObject);

    /**
     * 按 field value 去重，返回指定数目的 doc
     *
     * @param esObject 去重请求参数
     * @return ES 通用响应结果
     */
    SearchBaseResult<Map<String, ESResponse>> collapse(CollapseQueryObject esObject);
}

