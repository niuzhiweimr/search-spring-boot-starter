package com.elastic.search.common.exception;



/**
 * @author niuzhiwei
 */
public class OpenSystemException extends SystemException {

    private static final long serialVersionUID = -427919774313417519L;

    public OpenSystemException(int errorCode, String desc) {
        super(errorCode, desc);
    }

    public OpenSystemException(ErrorCode errorCode) {
        this(errorCode.getErrorCode(), errorCode.getErrorReason());
    }

    @Override
    public String getMessage() {
        return getErrorCode() + "-" + getErrorReason();
    }
}
