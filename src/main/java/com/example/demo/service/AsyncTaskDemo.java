package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author zhangzongbo
 * @date 18-12-21 下午2:12
 */

@Component
@Slf4j
public class AsyncTaskDemo {


    @Async("myExecutor")
    public void doTask(String url){

        Random random = new Random();
        log.info("Task begin ");
        long start = System.currentTimeMillis();
        log.info("=======> begin download url: {}",url);
        try {
            Thread.sleep(random.nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("Task end cost time: {}",System.currentTimeMillis() - start);
    }
}
