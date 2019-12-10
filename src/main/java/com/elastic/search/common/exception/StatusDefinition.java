package com.elastic.search.common.exception;


import com.elastic.search.common.domain.SearchBaseResult;
import org.apache.commons.lang3.StringUtils;

/**
 * @author niuzhiwei
 */
public interface StatusDefinition {

    /**
     * 获取编码
     *
     * @return
     */
    int getErrorCode();

    /**
     * 获取描述
     *
     * @return
     */
    String getErrorReason();

    /**
     * 抛出异常
     *
     * @return
     */
    default SystemException throwError() {
        throw new SystemException(this.getErrorCode(), this.getErrorReason());
    }

    /**
     * 抛出异常
     *
     * @param throwable
     * @return
     */
    default SystemException throwError(Throwable throwable) {
        throw new SystemException(this.getErrorCode(), this.getErrorReason()).setCause(throwable);
    }

    /**
     * 使用传入消息体作为返回给调用者的errorReason
     *
     * @param msg
     * @return
     */
    default SystemException throwError(String msg) {
        throw new SystemException(this.getErrorCode(), msg);
    }

    /**
     * 使用传入消息体作为返回给调用者的errorReason
     *
     * @param msg
     * @return
     */
    default SystemException throwError(String msg, Throwable throwable) {
        throw new SystemException(this.getErrorCode(), msg).setCause(throwable);
    }

    /**
     * 将传入的详细描述追加到默认errorReason后面
     * <p>
     * 例如：参数错误(`x`必须为数字类型)
     *
     * @param detail
     * @return
     */
    default SystemException throwErrorAndAppendDetail(String detail) {
        throw new SystemException(this.getErrorCode(), this.getErrorReason() + "(" + detail + ")");
    }

    /**
     * 将传入的详细描述追加到默认errorReason后面
     * <p>
     * 例如：参数错误(`x`必须为数字类型)
     *
     * @param detail
     * @return
     */
    default SystemException throwErrorAndAppendDetail(String detail, Throwable throwable) {
        throw new SystemException(this.getErrorCode(), this.getErrorReason() + "(" + detail + ")").setCause(throwable);
    }

    /**
     * 使用状态定义创建DataResult<?>
     *
     * @return
     */
    default SearchBaseResult<?> newDataResult() {
        return SearchBaseResult.faild(getErrorCode(), getErrorReason());
    }

    /**
     * 使用状态定义创建DataResult<?>
     *
     * @param msg 作为Status.statusReason使用
     * @return
     */
    default SearchBaseResult<?> newDataResult(String msg) {
        return SearchBaseResult.faild(getErrorCode(), msg);
    }

    /**
     * 追加错误明细并转化到DataResult
     *
     * @param errorDetail 错误明细
     * @return
     */
    default SearchBaseResult<?> asDataResult(String errorDetail) {
        return SearchBaseResult.faild(getErrorCode(), getErrorReason(errorDetail));
    }

    default String getErrorReason(String errorDetails) {
        if (StringUtils.isNotBlank(errorDetails)) {
            return getErrorReason() + "(" + errorDetails + ")";
        } else {
            return getErrorReason();
        }
    }
}
