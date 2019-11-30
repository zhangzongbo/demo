package com.example.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author zhangzongbo
 * @date 19-11-27 下午2:46
 */

@Slf4j
public class CyclicBarrierTest {

    public static void main(String[] args) {

        final int threadCount = 5;
        CyclicBarrier barrier = new CyclicBarrier(threadCount);

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < threadCount; i ++){
            executorService.execute(() -> {
                int waitTime = new Random (5).nextInt();
                log.info("{} 等待 {}", Thread.currentThread().getName(), waitTime);
                try {
                    TimeUnit.SECONDS.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("{} 到达 barrier", Thread.currentThread().getName( ));
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }

                log.info("{} barrier 结束", Thread.currentThread().getName());
            });
        }

        executorService.shutdown();

    }
}
