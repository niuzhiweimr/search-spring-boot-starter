package com.elastic.search.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author niuzhiwei
 */
public class Status implements Serializable {

    private static final long serialVersionUID = -8847081762490398492L;
    @JsonProperty("statusCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int statusCode = 0;
    @JsonProperty("statusReason")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String statusReason;

    public Status() {

    }

    public Status(int statusCode, String message) {
        this(statusCode, message, true);
    }

    /**
     * @param statusCode
     * @param message
     * @param appendSysCode 是否追加系统编码
     */
    public Status(int statusCode, String message, boolean appendSysCode) {
        if (appendSysCode) {
            if (statusCode != 0) {
                this.statusCode = SystemManager.appendSysCode(statusCode);
            }
        } else {
            this.statusCode = statusCode;
        }
        this.statusReason = message;
    }

    @Override
    public String toString() {
        return "{\"statusCode\":" + statusCode + ",\"statusReason\":\"" + statusReason + "\"}";
    }

    public int getStatusCode() {
        return statusCode;
    }

    /**
     * 注意该种方式只在是使用序列化框架是使用，如果编码是调用请使用
     * <p/>
     * <code>
     * setStatusCode(int statusCode, boolean appendSysCode)
     * </code>
     *
     * @param statusCode
     */
    @SuppressWarnings("unused")
    private void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * 注意编程式方式调用
     *
     * @param statusCode
     * @param appendSysCode
     */
    public void setStatusCode(int statusCode, boolean appendSysCode) {
        if (statusCode != 0) {
            if (appendSysCode) {
                this.statusCode = SystemManager.appendSysCode(statusCode);
            } else {
                this.statusCode = statusCode;
            }
        }
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }


}
