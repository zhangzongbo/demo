package com.example.demo.service.dependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangzongbo
 * @date 18-12-25 下午3:48
 */

@Component
public class CircularDependencyB {

    private CircularDependencyA circularDependencyA;

    private String message = "zobgo";

    public String getMessage(){
        return message;
    }

    @Autowired
    public void setCircularDependencyA(CircularDependencyA circularDependencyA){
        this.circularDependencyA = circularDependencyA;
    }

    public CircularDependencyA getCircularDependencyA(){
        return circularDependencyA;
    }

}
