package com.example.demo.entity;

import lombok.Data;
/**
 * @author zhangzongbo
 * @date 18-12-25 下午6:22
 */

@Data
public class JsonResult<T> {
    private String code;
    private String message;
    private T data;

    public JsonResult(String code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public JsonResult(){
        super();
    }

    public JsonResult(T data){
        this("200","success",data);
    }

    public static<E> JsonResult ok(){
        return new JsonResult<>("200","success",null);
    }

    public static<E> JsonResult<E> ok(E data){
        return new JsonResult<>("200", "success", data);
    }
}
