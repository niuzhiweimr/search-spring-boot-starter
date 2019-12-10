package com.elastic.search.common.domain;

import com.google.gson.Gson;
import com.elastic.search.common.exception.ErrorCode;
import com.elastic.search.common.exception.RpcException;

import java.io.Serializable;

/**
 * @author niuzhiwei
 */
abstract class BaseResult implements Serializable {

    private static final long serialVersionUID = 610503329211184167L;

    /**
     * 获取状态
     *
     * @return
     */
    public abstract Status getStatus();

    @Deprecated
    public void throwOpenException() {
        throwRpcException();
    }

    public void throwRpcException() {
        if (getStatus().getStatusCode() != 0) {
            throw new RpcException(getStatus().getStatusCode(), getStatus().getStatusReason());
        }
    }

    /**
     * 断言RPC OK 全部成功或部分成功
     */
    public void assertRpcOK() {

        if (getStatus().getStatusCode() != 0 && getStatus().getStatusCode() != ErrorCode.PART_ERROR.getErrorCode()) {
            throwRpcException();
        }
    }

    /**
     * 断言RPC调用全部成功
     */
    public void assertRpcAllSuccess() {
        if (getStatus().getStatusCode() != 0) {
            throwRpcException();
        }
    }


    private static final Gson gson = new Gson();


    public String toJSON() {
        return gson.toJson(this);
    }
}