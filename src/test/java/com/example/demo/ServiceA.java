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
public class ServiceA {

    private ServiceB serviceB;

    public ServiceA(){
        log.info("ServiceA()");
    }

    void foo(){
        log.info("ServiceA.foo()");
//        serviceB.foo();
    }

    @Autowired
    public void setServiceB(ServiceB serviceB){
        log.info("ServiceA.setServiceB()");
        this.serviceB = serviceB;
    }

}
