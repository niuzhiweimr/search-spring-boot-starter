package com.elastic.search.common.exception;

/**
 * RPC 调用异常类，例如A服务 调用->B服务时，如果B服务返回消息为非成功状态时，
 * 抛出这类异常，这类异常在DataResult中不会增加当前系统的系统编码。
 *
 * @author niuzhiwei
 */
public class RpcException extends SystemException {

    private static final long serialVersionUID = -8747374925065155821L;

    public RpcException(int errorCode, String errorReason) {
        super(errorCode, errorReason);
    }
}
