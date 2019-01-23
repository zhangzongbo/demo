package com.example.demo.aspect;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.LogWrapper;
import com.example.demo.wrapper.ReadableHttpRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author zhangzongbo
 * @date 18-12-25 下午7:53
 *
 * AOP切片 打印日志有缺陷,参数校验失败失败时不能打印日志,弃用,改用filter
 */
@Aspect
//@Component
@Slf4j
public class WebLogAspect {

    private static final int BUFFER_SIZE = 1024;

    @Pointcut("execution(public * com.example.demo.controller..*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {


        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        ReadableHttpRequestWrapper requestWrapper = new ReadableHttpRequestWrapper(request);

        LogWrapper wrapper = LogWrapper.builder()
                .url(request.getRequestURL().toString())
                .httpMethod(request.getMethod())
                .ip(request.getRemoteAddr())
                .classMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName())
//                .params(JSONObject.toJSONString(request.getParameterMap()))
                .params(requestWrapper.getBody())
                .build();
        log.info("<<=== {}", wrapper.toString());


    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        log.info("===>> RESPONSE : {}", JSONObject.toJSONString(ret));
    }

}
