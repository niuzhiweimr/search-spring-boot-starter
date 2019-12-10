package com.elastic.search.common.domain;

/**
 * <p>
 * es 搜索引擎错误码定义
 * <p>
 * 1(操作类型) + 00（错误码）；
 * 操作类型 0 通用操作；1 索引操作； 2 删除操作；3 更新操作； 4 查询操作；
 *
 * @author niuzhiwei
 */
public class ESErrorCode {

    /**
     * Escenter 异常
     */
    public static final int ESCENTER_ERROR_CODE = 5555;

    /**
     * 索引发送mq消息失败
     */
    public static final int INDEX_SEND_MQ_ERROR_CODE = 5001;

    /**
     * Client 异常
     */
    public static final int CLIETN_ERROR_CODE = 1111;

    /**
     * 请求参数错误
     */
    public static final int REQUEST_PARAM_ERROR_CODE = 1001;

    /**
     * 索引请求异常
     */
    public static final int INDEX_ERROR_CODE = 2222;
    /**
     * 重复索引请求
     */
    public static final int REPETITION_INDEX_ERROR_CODE = 2001;

    /**
     * Elastic 异常
     */
    public static final int ELASTIC_ERROR_CODE = 9999;

    /**
     * 索引不存在
     */
    public static final int INDEX_NOT_EXIST_ERROR_CODE = 9001;

    /**
     * 查询错误
     */
    public static final int QUERY_PHASE_ERROR_CODE = 9002;

    /**
     * 文档不存在
     */
    public static final int DOC_NOT_EXIST_ERROR_CODE = 9002;
}
