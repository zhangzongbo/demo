package com.example.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author zhangzongbo
 * @date 19-11-29 下午4:12
 */

@Slf4j
public class ThreadPoolTest {

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(5);

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 5, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    }
}
