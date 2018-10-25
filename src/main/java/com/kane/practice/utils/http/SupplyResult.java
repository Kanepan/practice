package com.kane.practice.utils.http;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class SupplyResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static int STATUS_FAILED = 0; // 失败
    public static int STATUS_SUCCESS = 1; // 成功
    public static int STATUS_UNCONFIRM = 2; // 超时或者未确认状态
    private Integer status = STATUS_UNCONFIRM;

    private boolean isAsync = true; // 是否异步供货， 如果是异步供货 返回状态是 STATUS_SUCCESS
    // 则表示提交成功

    private String resultCode;

    private String resultMsg;

    private T module;

    public boolean isSuccess() {
        return status == STATUS_SUCCESS;
    }

    public boolean isFailed() {
        return status == STATUS_FAILED;
    }

    public boolean isUnConfirm() {
        return status == STATUS_UNCONFIRM;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getModule() {
        return module;
    }

    public void setModule(T module) {
        this.module = module;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        if (resultMsg != null) {
            return resultMsg.replace("_", "");
        }
        return null;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public void setAsync(boolean isAsync) {
        this.isAsync = isAsync;
    }

    public void setSuccess() {
        this.status = STATUS_SUCCESS;
    }

    public void setFailed() {
        this.status = STATUS_FAILED;
    }

    public void setUnconfirm() {
        this.status = STATUS_UNCONFIRM;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

}
