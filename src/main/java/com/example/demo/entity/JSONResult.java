package com.example.demo.entity;
import lombok.Data;

/**
 * @author zhangzongbo
 * @date 18-12-25 下午6:22
 */

@Data
public class JSONResult<T> {
    private String code;
    private String message;
    private T data;

    private JSONResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public JSONResult() {
        super();
    }

    public JSONResult(T data) {
        this("200", "SUCCESS", data);
    }

    public static <E> JSONResult<E> ok() {
        return new JSONResult<>("200", "SUCCESS", null);
    }

    public static <E> JSONResult<E> ok(E data) {
        return new JSONResult<>("200", "SUCCESS", data);
    }

    public static <E> JSONResult<E> ok(String message, E data) {
        return new JSONResult<>("200", message, data);
    }

    public static <E> JSONResult<E> ok(String code, String message, E data) {
        return new JSONResult<>(code, message, data);
    }

    public static <E> JSONResult<E> error() {
        return new JSONResult<>("400", "ERROR", null);
    }

    public static <E> JSONResult<E> error(E data) {
        return new JSONResult<>("400", "ERROR", data);
    }

    public static <E> JSONResult<E> error(String message, E data) {
        return new JSONResult<>("400", message, data);
    }

    public static <E> JSONResult<E> error(String code, String message, E data) {
        return new JSONResult<>(code, message, data);
    }
}
