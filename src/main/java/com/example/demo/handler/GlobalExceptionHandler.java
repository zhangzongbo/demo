package com.example.demo.handler;

import com.example.demo.entity.JSONResult;
import com.example.demo.exception.CustomerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

/**
 * @author zhangzongbo
 * create on 18-12-25 下午6:20
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public JSONResult<String> defaultHandler(Exception e) {
        printLog(e);

        return JSONResult.error("-1", "未知错误", "{}");
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public JSONResult<String> paramsMissing(MissingServletRequestParameterException e) {
        printLog(e);

        String message = String.format("缺少必要参数,参数名称为%s", e.getParameterName());

        return JSONResult.error("-1", message, "{}");
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    public JSONResult<String> paramsTypeNotMatch(TypeMismatchException e) {
        printLog(e);
        String message = String.format("参数类型不匹配,参数%s, 值：%s 类型应为%s", ((MethodArgumentTypeMismatchException) e).getName(), e.getValue(), e.getRequiredType());

        return JSONResult.error("-1", message, "{}");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public JSONResult<Object> notValidException(MethodArgumentNotValidException e) {
        printLog(e);

        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError == null ? "参数校验失败" : fieldError.getDefaultMessage();
        Object data = fieldError == null ? e.getBindingResult().getAllErrors().get(0).getDefaultMessage() : fieldError.getField();
        return JSONResult.error("-1", message, data);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public JSONResult<String> methodError(HttpRequestMethodNotSupportedException e) {
        printLog(e);

        String message = "请求方法错误,需要 " + Arrays.toString(e.getSupportedMethods());
        return JSONResult.error("-1", message, "{}");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public JSONResult<String> mediaTypeError(HttpMediaTypeNotSupportedException e) {
        printLog(e);
        return JSONResult.error("-1", "mediaType Error! " + e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public JSONResult<String> paramRequired(HttpMessageNotReadableException e) {
        printLog(e);
        return JSONResult.error("-1", "required request body is missing", e.getLocalizedMessage());
    }

    @ExceptionHandler(CustomerException.class)
    @ResponseBody
    public JSONResult<String> customerException(CustomerException e){
        printLog(e);
        return JSONResult.error("-1", e.getMessage(), "{}");
    }

    private void printLog(Exception e){
        log.error("message: {}", e.getMessage(), e);
    }
}
