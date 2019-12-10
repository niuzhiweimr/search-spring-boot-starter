package com.elastic.search.common.exception;

import com.elastic.search.common.domain.SearchBaseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * @author niuzhiwei
 */
public class ExceptionUtils {

    /**
     * 超时异常
     *
     * @param ex
     * @return
     */
    public static boolean isTimeOutException(Throwable ex) {
        if (TimeoutException.class.isAssignableFrom(ex.getClass())) {
            return true;
        } else if (SocketTimeoutException.class.isAssignableFrom(ex.getClass())) {
            return true;
        }

        if (ex.getCause() != null) {
            return isTimeOutException(ex.getCause());
        }
        return false;
    }

    /**
     * 连接异常
     *
     * @param throwable
     * @return
     */
    public static boolean isConnectionException(Throwable throwable) {
        return isConnectException(throwable) || isSocketConnectTimeout(throwable);
    }

    private static boolean isConnectException(Throwable throwable) {
        if (ConnectException.class.isAssignableFrom(throwable.getClass())) {
            return true;
        }
        if (throwable.getCause() != null) {
            return isConnectException(throwable.getCause());
        }
        return false;
    }

    private static boolean isSocketConnectTimeout(Throwable throwable) {
        if (SocketTimeoutException.class.isAssignableFrom(throwable.getClass())) {
            if (throwable.getMessage() != null) {
                return "connect timed out".equalsIgnoreCase(throwable.getMessage().trim());
            }
        }
        if (throwable.getCause() != null) {
            return isSocketConnectTimeout(throwable.getCause());
        }
        return false;
    }

    /**
     * 获取异常消息
     *
     * @param throwable
     * @return
     */
    public static String getExceptionMsg(Throwable throwable) {
        if (throwable != null) {
            if (StringUtils.isNoneBlank(throwable.getMessage())) {
                return throwable.getMessage();
            } else {
                if (throwable.getCause() != null) {
                    return getExceptionMsg(throwable.getCause());
                }
            }
        }
        return null;
    }

    /**
     * 获取深度异常消息描述
     *
     * @param th
     * @return
     */
    public static String getDepthExceptionMsg(Throwable th) {
        if (th.getCause() != null) {
            Throwable throwable = th.getCause();
            String msg = getDepthExceptionMsg(throwable);
            if (StringUtils.isNotBlank(msg)) {
                return msg;
            } else {
                return throwable.getMessage();
            }
        } else {
            return th.getMessage();
        }
    }

    public static final String NO_AVAILABLE_SERVER = "Load balancer does not have available server for client";


    /**
     * 用户自定义异常(异常来自SystemException或其子类)
     *
     * @param throwable
     * @return
     */
    public static boolean isUserDefinedException(Throwable throwable) {
        if (throwable != null) {
            if (SystemException.class.isAssignableFrom(throwable.getClass())) {
                return true;
            } else {
                return isUserDefinedException(throwable.getCause());
            }
        }
        return false;
    }

    /**
     * 获取用户自定义的系统异常,如果获取不到则使用 参数：{defaultVal}默认值构建一个系统异常
     *
     * @param throwable 异常对象实例
     * @return
     */
    public static SystemException getUserDefinedException(Throwable throwable) {
        if (throwable != null) {
            if (SystemException.class.isAssignableFrom(throwable.getClass())) {
                return (SystemException) throwable;
            } else {
                return getUserDefinedException(throwable.getCause());
            }
        }
        return null;
    }

    public static SearchBaseResult<?> asDataResult(Throwable throwable) {
        String errorDetails = ExceptionUtils.getDepthExceptionMsg(throwable);
        if (isParamMissing(throwable)) {// 参数错误
            return ErrorCode.PARAM_ERROR.asDataResult(errorDetails);
        } else if (ExceptionUtils.isUserDefinedException(throwable)) {// 用户自定义异常
            SystemException systemException = ExceptionUtils.getUserDefinedException(throwable);
            if (systemException != null) {
                return SearchBaseResult.faild(systemException.getErrorCode(), systemException.getErrorReason());
            }
        }
        if (ExceptionUtils.isConnectionException(throwable)) {
            return format(ErrorCode.CONNECT_ERROR, throwable, errorDetails);
        } else if (ExceptionUtils.isTimeOutException(throwable)) {
            return format(ErrorCode.TIME_OUT, throwable, errorDetails);
        } else {
            return format(ErrorCode.OTHER_ERROR, throwable, errorDetails);
        }
    }

    private static SearchBaseResult<?> format(ErrorCode errorCode, Throwable throwable, String errMsg) {
        if (isHystrixRuntimeException(throwable)) {
            String rawErrMsg = getHystrixRuntimeExceptionMsg(throwable);
            if (StringUtils.isNotBlank(rawErrMsg)) {
                return errorCode.asDataResult(rawErrMsg);
            }
        }
        return errorCode.asDataResult(errMsg);
    }

    public static boolean isHystrixRuntimeException(Throwable throwable) {
        if (throwable != null) {
            if ("com.netflix.hystrix.exception.HystrixRuntimeException".equals(throwable.getClass().getName())) {
                return true;
            }
            return isHystrixRuntimeException(throwable.getCause());
        }
        return false;
    }

    public static String getHystrixRuntimeExceptionMsg(Throwable throwable) {
        if (throwable != null) {
            if ("com.netflix.hystrix.exception.HystrixRuntimeException".equals(throwable.getClass().getName())) {
                return throwable.getMessage();
            }
            return getHystrixRuntimeExceptionMsg(throwable.getCause());
        }
        return null;
    }

    public static boolean isParamMissing(Throwable ex) {
        for (Class<?> clazz : PARAM_EXCEPTION) {
            if (ex.getClass() == clazz) {
                return true;
            }
        }
        return false;
    }

    public static final Class<?>[] PARAM_EXCEPTION = new Class[]{
            BindException.class,
            IllegalArgumentException.class,
    };
}
