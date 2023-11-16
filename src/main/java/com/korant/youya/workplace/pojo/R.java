package com.korant.youya.workplace.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName R
 * @Description
 * @Author chenyiqiang
 * @Date 2023/7/18 11:14
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String respCode;
    private String respMsg;
    private T data;
    private long timestamp = System.currentTimeMillis();

    public static <T> R<T> ok() {
        return buildResult(true, "0000", null, "成功");
    }

    public static <T> R<T> ok(String message) {
        return buildResult(true, "0000", null, message);
    }

    public static <T> R<T> success(T t) {
        return buildResult(true, "0000", t, null);
    }

    public static <T> R<T> ok(T t, String message) {
        return buildResult(true, "0000", t, message);
    }

    public static <T> R<T> error(String respCode, String message) {
        return buildResult(false, respCode, null, message);
    }

    public static <T> R<T> error(String message) {
        return buildResult(false, "9999", null, message);
    }

    private static <T> R<T> buildResult(boolean success, String code, T t, String message) {
        R<T> result = new R<T>();
        result.setSuccess(success);
        result.setRespCode(code);
        result.setData(t);
        result.setRespMsg(message);
        return result;
    }
}

