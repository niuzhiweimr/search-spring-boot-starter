package com.elastic.search.common.exception;

/**
 * @author niuzhiwei
 */
public class SearchBusinessException extends BusinessException {

    private static final long serialVersionUID = -3460524052435971680L;

    public SearchBusinessException(Throwable t) {
        super(t);
    }

    public SearchBusinessException(Integer errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public SearchBusinessException(Integer errorCode, String errorMsg, Throwable cause) {
        super(errorCode, errorMsg, cause);
    }

}
