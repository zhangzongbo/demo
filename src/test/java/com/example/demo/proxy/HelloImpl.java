package com.example.demo.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangzongbo
 * @date 19-12-4 下午4:18
 */

@Slf4j
public class HelloImpl implements HelloInterface {


    @Override
    public void sayHello() {
        log.info("hello");
    }
}
