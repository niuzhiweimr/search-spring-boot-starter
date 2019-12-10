package com.elastic.search.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.elastic.search.common.exception.ErrorCode;
import com.elastic.search.common.exception.StatusDefinition;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用RPC数据序列化传递对象
 *
 * @author niuzhiwei
 */
public class SearchBaseResult<T> extends BaseResult {

    private static final long serialVersionUID = 3335774490588365954L;
    /**
     * 解析协议状态码
     */
    @JsonProperty("status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Status status = new Status(0, "");
    /**
     * 返回Result
     */
    @JsonProperty("result")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;
    /**
     * 附加属性/扩展属性
     */
    @JsonProperty("attachment")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> attachment;

    @Override
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static SearchBaseResult<Object> success(Object result) {
        SearchBaseResult<Object> searchResult = new SearchBaseResult<Object>();
        searchResult.setResult(result);
        return searchResult;
    }

    /**
     * 部分成功
     *
     * @param result
     * @return
     */
    public static SearchBaseResult<Object> part(Object result) {
        SearchBaseResult<Object> searchResult = new SearchBaseResult<Object>();
        searchResult.setResult(result);
        searchResult.status.setStatusCode(ErrorCode.PART_ERROR.getErrorCode(), true);
        searchResult.status.setStatusReason(ErrorCode.PART_ERROR.getErrorReason());
        return searchResult;
    }

    /**
     * 创建部分成功的结构体
     *
     * @param key
     * @param value
     * @return
     */
    public static SearchBaseResult<Map<String, Object>> makePart(String key, Object value) {
        SearchBaseResult<Map<String, Object>> searchResult = new SearchBaseResult<>();
        searchResult.setResult(new HashMap<>());
        searchResult.result.put(key, value);
        searchResult.status.setStatusCode(ErrorCode.PART_ERROR.getErrorCode(), true);
        searchResult.status.setStatusReason(ErrorCode.PART_ERROR.getErrorReason());
        return searchResult;
    }

    @SuppressWarnings("unchecked")
    public static <T> SearchBaseResult<T> success(Object result, Class<T> type) {
        SearchBaseResult<T> searchResult = new SearchBaseResult<T>();
        searchResult.setResult((T) result);
        return searchResult;
    }

    public static <T> SearchBaseResult<T> faild(int errorCode, String errMsg) {
        SearchBaseResult<T> searchResult = new SearchBaseResult<T>();
        searchResult.setStatus(new Status(errorCode, errMsg));
        return searchResult;
    }

    /**
     * 当RPC调用失败时，使用该方式包装处理，该处理方式不会appeng当前系统编码
     *
     * @param errorCode
     * @param errMsg
     * @return
     */
    public static <T> SearchBaseResult<T> rpcFaild(int errorCode, String errMsg) {
        SearchBaseResult<T> searchResult = new SearchBaseResult<T>();
        searchResult.setStatus(new Status(errorCode, errMsg, false));
        return searchResult;
    }

    /**
     * 返回的状态码自动最加当前系统编码
     *
     * @param statusDefinition
     * @return
     */
    public static <T> SearchBaseResult<T> faild(StatusDefinition statusDefinition) {
        SearchBaseResult<T> searchResult = new SearchBaseResult<T>();
        searchResult.setStatus(new Status(statusDefinition.getErrorCode(), statusDefinition.getErrorReason()));
        return searchResult;
    }

    public static SearchBaseResult<Map<String, Object>> make(String key, Object value) {
        SearchBaseResult<Map<String, Object>> searchResult = new SearchBaseResult<>();
        searchResult.setResult(new HashMap<>());
        searchResult.result.put(key, value);
        return searchResult;
    }

    @SuppressWarnings("unchecked")
    public SearchBaseResult<Map<String, Object>> addResult(String key, Object value) {
        Map<String, Object> result = (Map<String, Object>) this.getResult();
        result.put(key, value);
        return (SearchBaseResult<Map<String, Object>>) this;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return getStatus().getStatusCode() == 0;
    }

    /**
     * 接口返回错误
     */
    @JsonIgnore
    public boolean isFailed() {
        return !isSuccess();
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void addAttachment(String key, Object value) {
        if (attachment == null) {
            this.attachment = new HashMap<>();
        }
        this.attachment.put(key, value);
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
     * 默认构造器
     */
    public SearchBaseResult() {
    }
}