package com.example.demo.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zhangzongbo
 * @date 19-12-4 下午4:19
 */

@Slf4j
public class HelloProxyHandler implements InvocationHandler {

    private Object subject;

    public HelloProxyHandler(Object subject){
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object result = null;

        log.info("可以在调用实际方法前做一些事情");

        log.info("当前调用的方法是: {}", method.getName());

        result = method.invoke(subject, args);
        log.info("{} 方法的返回值是: {}" ,method.getName() , result);

        log.info("可以在调用实际方法后做一些事情");
        return result;
    }
}
