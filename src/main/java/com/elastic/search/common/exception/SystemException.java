package com.elastic.search.common.exception;

/**
 * @author niuzhiwei
 */
public class SystemException extends RuntimeException {

    private static final long serialVersionUID = -8995909169223941100L;
    /**
     * 错误码
     */
    private int errorCode;
    /**
     * 错误描述
     */
    private String errorReason;

    public SystemException(int errorCode, String errorReason) {
        super("errorCode:"+errorCode+"-errorReason:"+errorReason);
        this.errorCode = errorCode;
        this.errorReason = errorReason;
    }

    public SystemException(ErrorCode errorCode) {
        this(errorCode.getErrorCode(), errorCode.getErrorReason());
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public SystemException setCause(Throwable t) {
        super.initCause(t);
        return this;
    }
}
