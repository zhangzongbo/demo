package com.example.demo.service.dependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangzongbo
 * @date 18-12-25 下午3:48
 */

@Component
public class CircularDependencyA {

    private CircularDependencyB circularDependencyB;

    private String message = "lala";

    public String getMessage(){
        return message;
    }

    @Autowired
    public void  setCircularDependencyB (CircularDependencyB circularDependencyB){
        this.circularDependencyB = circularDependencyB;
    }

    public CircularDependencyB getCircularDependencyB(){
        return circularDependencyB;
    }

}
