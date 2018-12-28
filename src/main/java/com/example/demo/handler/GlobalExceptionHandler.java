package com.example.demo.handler;

import com.example.demo.entity.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author zhangzongbo
 * @date 18-12-25 下午6:20
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public JsonResult defaultHandler(HttpServletRequest request, Exception e) throws Exception{
        e.printStackTrace();
        log.error(e.getMessage());
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode("-1");
        jsonResult.setMessage("未知错误");
        jsonResult.setData("{}");
        return jsonResult;
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public JsonResult paramsMissing(MissingServletRequestParameterException e){
        log.error(e.getMessage());
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode("-1");
        jsonResult.setMessage(String.format( "缺少必要参数,参数名称为%s",e.getParameterName()));
        jsonResult.setData("{}");
        return jsonResult;
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    public JsonResult paramsTypeNotMatch(TypeMismatchException e){
        log.error(e.getMessage());
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode("-1");
        jsonResult.setMessage(String.format( "参数类型不匹配,参数%s, 值：%s 类型应为%s",((MethodArgumentTypeMismatchException) e).getName(),e.getValue(),e.getRequiredType()));
        jsonResult.setData("{}");
        return jsonResult;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public JsonResult notValidException(MethodArgumentNotValidException e){
        log.error(e.getMessage());
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode("-1");
        jsonResult.setMessage("参数检验失败");
        jsonResult.setData(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return jsonResult;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public JsonResult methodError(HttpRequestMethodNotSupportedException e){
        log.error(e.getMessage());
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode("-1");
        jsonResult.setMessage("请求方法错误,需要" + Arrays.toString(e.getSupportedMethods()));
        jsonResult.setData("{}");
        return jsonResult;
    }
}
