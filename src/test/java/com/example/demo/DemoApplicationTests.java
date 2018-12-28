package com.example.demo;

import com.example.demo.service.dependency.CircularDependencyA;
import com.example.demo.service.dependency.CircularDependencyB;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = "com.example.demo.service.dependency")
public class DemoApplicationTests {

    @Autowired
    ApplicationContext context;

    @Autowired
    private CircularDependencyA circularDependencyA;

    @Autowired
    private CircularDependencyB circularDependencyB;

    @Test
    public void contextLoads() {

        String[] beans = context.getBeanDefinitionNames();
        for (String bean : beans){
            log.info(bean);
        }
    }

    @Test
    public void circularTest(){
         log.info("hello: {}", circularDependencyA.getCircularDependencyB().getMessage());
    }

}

