package com.elastic.search.elasticsearch.infrastructure.utils;


import com.elastic.search.elasticsearch.dataobject.conditions.OrderCondition;
import com.elastic.search.elasticsearch.dataobject.enums.SortEnum;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 搜索引擎排序条件工具类
 *
 * @author niuzhiwei\
 */
public class OrderConditionUtils {

    private OrderConditionUtils() {

    }

    private static final ThreadLocal<List<OrderCondition>> LIST = new ThreadLocal<>();

    public static OrderConditionUtils start() {
        OrderConditionUtils utils = new OrderConditionUtils();
        LIST.set(new ArrayList<>());
        ;
        return utils;
    }

    public OrderConditionUtils addCondition(String fieldName, SortEnum sort) {
        OrderCondition order = new OrderCondition();
        order.setFieldName(fieldName);
        order.setOrderCondition(sort);
        LIST.get().add(order);
        return this;
    }

    public List<OrderCondition> end() {
        return LIST.get();
    }

    @Override
    protected void finalize() {
        LIST.remove();
    }
}
