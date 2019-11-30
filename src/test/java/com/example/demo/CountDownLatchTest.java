package com.example.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangzongbo
 * @date 19-11-26 上午11:57
 */

@Slf4j
public class CountDownLatchTest {


    public static void main(String[] args) throws InterruptedException {

        int threadCount = 5;
        CountDownLatch latch = new CountDownLatch(threadCount);
        Service service = new Service(latch);
        Runnable task = service::exec;

        for (int i = 0; i < threadCount; i ++){
            Thread thread = new Thread(task);
            thread.start();
        }

        log.info("main thread latch await. ");

        latch.await();

        log.info("main thread await finish. ");

    }

    public static class Service{

        private CountDownLatch latch;

        Service(CountDownLatch latch){
            this.latch = latch;
        }

        private void exec(){
            try {
                log.info("{} begin execute task. ", Thread.currentThread().getName());
                sleep(2);
                log.info("{} finish execute task. ", Thread.currentThread().getName());
            }finally {
                latch.countDown();
            }
        }

        private void sleep(Integer sec){
            try {
                TimeUnit.SECONDS.sleep(sec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
