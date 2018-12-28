package com.example.demo.task;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author zhangzongbo
 * @date 18-12-24 下午7:12
 */

@Slf4j
public class DemoTask implements Runnable{

    private Random random = new Random();
    private String url;
    public DemoTask(String url){
        this.url = url;
    }


    public void start(String url){

    }

    @Override
    public void run() {
        log.info("task begin:: ");
        long start = System.currentTimeMillis();

        try {
            Thread.sleep(random.nextInt(10000));
            log.info("====||====||====||====url: {} Thread: {} COST: {} ms====||====||====||====",
                    url,Thread.currentThread().getName(),System.currentTimeMillis() - start);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
