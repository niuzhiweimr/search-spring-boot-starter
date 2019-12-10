package com.elastic.search.common.exception;

/**
 * @author niuzhiwei
 */
public class ExceptionFactory {

    public ExceptionFactory() {
    }

    public static synchronized SearchBusinessException ex(Throwable throwable) {
        return new SearchBusinessException(throwable);
    }

    public static synchronized SearchBusinessException ex(Integer errorCode, String errorMsg) {
        return new SearchBusinessException(errorCode, errorMsg);
    }

    public static synchronized SearchBusinessException ex(Integer errorCode, String errorMsg, Object... regex) {
        return new SearchBusinessException(errorCode, String.format(errorMsg, regex));
    }

    public static synchronized SearchBusinessException ex(Integer errorCode, Throwable throwable, String errorMsg, Object... regex) {
        return new SearchBusinessException(errorCode, String.format(errorMsg, regex), throwable);
    }

}
