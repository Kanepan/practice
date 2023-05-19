package com.kane.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static Integer SUCCESS_CODE = 200;
    private static Integer FAILED_CODE = 500;

    private static final long serialVersionUID = -8811713146131862993L;

    private T data;
    private Integer code;
    private String message;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }

    public boolean isFailed() {
        return !isSuccess();
    }

    public static <T> Result<T> succeed(T model) {
        return of(model, SUCCESS_CODE, "success");
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return of(model, SUCCESS_CODE, msg);
    }

    public static Result succeed() {
        return of(null, SUCCESS_CODE, "success");
    }

    public static <T> Result<T> of(T datas, Integer code, String msg) {
        return new Result<>(datas, code, msg);
    }

    public static Result failed(String msg) {
        return of(null, FAILED_CODE, msg);
    }

    public static Result failed(Integer code,String msg) {
        return of(null, code, msg);
    }
    
}
