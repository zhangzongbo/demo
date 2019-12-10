package com.example.demo.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author zhangzongbo
 * @date 19-12-5 下午6:08
 */

@Slf4j
public class HelloProxyCgLib implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public Object getProxy(Class clazz){

        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        log.info("可以在调用实际方法前做一些事情");

        Object result = methodProxy.invokeSuper(o, objects);

        log.info("可以在调用实际方法后做一些事情");
        return result;
    }
}
