package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author zhangzongbo
 * @date 19-12-6 下午5:34
 */
@Component
@Slf4j
public class ServiceB{

    private ServiceA serviceA;

    public ServiceB(){
        log.info("ServiceB()");
    }

    void foo(){
        log.info("ServiceB.foo()");
//        serviceA.foo();
    }

    @Autowired
    public void setServiceA(ServiceA serviceA){
        log.info("ServiceA.setServiceB()");
        this.serviceA = serviceA;
    }
}
