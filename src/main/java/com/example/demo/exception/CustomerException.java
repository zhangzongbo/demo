package com.example.demo.exception;

/**
 * @author zhangzongbo
 * @date 18-12-25 下午7:41
 */
public class CustomerException extends RuntimeException{
    public CustomerException(String message){
        super(message);
    }
}
