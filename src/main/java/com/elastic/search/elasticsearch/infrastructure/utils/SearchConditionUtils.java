package com.elastic.search.elasticsearch.infrastructure.utils;


import com.elastic.search.elasticsearch.dataobject.conditions.SearchCondition;
import com.elastic.search.elasticsearch.dataobject.enums.ConditionExpressionEnum;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 搜索工具类
 *
 * @author niuzhiwei
 */
public class SearchConditionUtils {


    private SearchConditionUtils() {
    }

    private static final ThreadLocal<List<SearchCondition>> LIST = new ThreadLocal<>();


    /**
     * 创建容器
     *
     * @return
     */
    public static SearchConditionUtils start() {
        SearchConditionUtils utils = new SearchConditionUtils();
        LIST.set(new ArrayList<SearchCondition>());
        return utils;
    }


    /**
     * 添加单条件单值
     *
     * @return
     */
    public SearchConditionUtils addCondition(String fieldName, Object fieldValue, ConditionExpressionEnum type) {
        SearchCondition searchCondition = new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setConditionExpression(type)
                .setSingleValue(fieldValue).build();
        LIST.get().add(searchCondition);
        return this;
    }

    /**
     * 添加单条件范围值
     *
     * @return
     */
    public SearchConditionUtils addCondition(String fieldName, Object minValue, Object maxValue, ConditionExpressionEnum type) {
        SearchCondition searchCondition = new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setConditionExpression(type)
                .setMinValue(minValue.toString())
                .setMxValue(maxValue.toString()).build();
        LIST.get().add(searchCondition);
        return this;
    }

    /**
     * 添加单条件多值
     *
     * @param fieldName
     * @param fieldValues
     * @param type
     * @return
     */
    public SearchConditionUtils addCondition(String fieldName, Object[] fieldValues, ConditionExpressionEnum type) {
        SearchCondition searchCondition = new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setConditionExpression(type)
                .setFeldValues(fieldValues).build();
        LIST.get().add(searchCondition);
        return this;
    }

    /**
     * 新增单值相等条件
     *
     * @param fieldName  属性名
     * @param fieldValue 属性值
     * @return self
     */
    public SearchConditionUtils addEqualCondition(String fieldName, Object fieldValue) {
        SearchCondition searchCondition = new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setConditionExpression(ConditionExpressionEnum.EQUAL)
                .setSingleValue(fieldValue).build();
        LIST.get().add(searchCondition);
        return this;
    }

    public SearchConditionUtils addBetweenEqualCondition(String fieldName, Object minValue, Object maxValue) {
        SearchCondition searchCondition = new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setMxValue(maxValue.toString())
                .setMinValue(minValue.toString())
                .setConditionExpression(ConditionExpressionEnum.BETWEEN_AND).build();
        LIST.get().add(searchCondition);
        return this;
    }

    /**
     * 获取条件结果
     *
     * @return
     */
    public List<SearchCondition> end() {
        return LIST.get();
    }


    /**
     * 创建等于查询条件
     *
     * @param fieldName  属性名
     * @param fieldValue 属性值
     * @return 查询条件
     */
    public static SearchCondition newEqualCondition(String fieldName, Object fieldValue) {
        return new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setConditionExpression(ConditionExpressionEnum.EQUAL)
                .setSingleValue(fieldValue).build();
    }


    /**
     * 创建不等于查询条件
     *
     * @param fieldName  属性名
     * @param fieldValue 属性值
     * @return 查询条件
     */
    public static SearchCondition newUnEqualCondition(String fieldName, Object fieldValue) {
        return new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setConditionExpression(ConditionExpressionEnum.UNEQUAL)
                .setSingleValue(fieldValue).build();
    }

    /**
     * 创建in查询条件
     *
     * @param fieldName   属性名
     * @param fieldValues 属性值数组
     * @return 查询条件
     */
    public static SearchCondition newInCondition(String fieldName, Object[] fieldValues) {
        return new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setConditionExpression(ConditionExpressionEnum.IN)
                .setFeldValues(fieldValues).build();
    }

    /**
     * 创建not in查询条件
     *
     * @param fieldName   属性名
     * @param fieldValues 属性值数组
     * @return 查询条件
     */
    public static SearchCondition newNotInCondition(String fieldName, Object[] fieldValues) {
        return new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setConditionExpression(ConditionExpressionEnum.NOT_IN)
                .setFeldValues(fieldValues).build();
    }

    /**
     * 创建between查询条件
     *
     * @param fieldName 属性名
     * @param minValue  属性值数组
     * @return 查询条件
     */
    public static SearchCondition newBetweenAndCondition(String fieldName, Object minValue, Object maxValue) {
        return new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setConditionExpression(ConditionExpressionEnum.BETWEEN_AND)
                .setMinValue(minValue.toString())
                .setMxValue(maxValue.toString()).build();
    }


    /**
     * 新建 like 查询条件
     *
     * @param fieldName  属性名
     * @param fieldValue 属性值
     * @return 查询条件
     */
    public static SearchCondition newLikeCondition(String fieldName, Object fieldValue) {
        return new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setConditionExpression(ConditionExpressionEnum.LIKE)
                .setSingleValue(fieldValue).build();
    }

    /**
     * 新建查询条件
     *
     * @param fieldName               属性名称
     * @param fieldValue              属性值
     * @param conditionExpressionEnum 条件
     * @return 查询条件
     */
    public static SearchCondition newSearchCondition(String fieldName, Object fieldValue, ConditionExpressionEnum conditionExpressionEnum) {
        return new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setConditionExpression(conditionExpressionEnum)
                .setSingleValue(fieldValue).build();
    }

    /**
     * 新建查询条件
     *
     * @param fieldName               属性名称
     * @param fieldValues             属性值列表
     * @param conditionExpressionEnum 条件
     * @return 查询条件
     */
    public static SearchCondition newSearchCondition(String fieldName, List<Object> fieldValues, ConditionExpressionEnum conditionExpressionEnum) {
        return new SearchCondition.Builder()
                .setFieldName(fieldName)
                .setConditionExpression(conditionExpressionEnum)
                .setFeldValues(fieldValues.toArray(new String[0])).build();
    }

    @Override
    protected void finalize() {
        LIST.remove();
    }
}
