package com.example.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhangzongbo
 * @date 19-11-20 下午5:40
 */

@Slf4j
public class AddTest {

    private static volatile int inc = 0;
    private static AtomicInteger atomicInc = new AtomicInteger();
    private Lock lock = new ReentrantLock();

    public static void main(String[] args) {

        int temp = 0;
        long start = System.currentTimeMillis();
        final AddTest test = new AddTest();

        for(int i=0;i<10;i++){
            new Thread(() -> {
                for(int j=0;j<1000;j++)
                    test.atomicAdd();
            }).start();
        }

        while (Thread.activeCount() > 1){
            /* 保证所有线程执行完毕 */
            Thread.yield();

            log.info("inc: {}", inc);
            if (temp == inc){
                log.info("cost: {}, inc: {}, temp: {} ", System.currentTimeMillis() - start, inc, temp);
                break;
            }
            temp = inc;
        }
    }

    /**
     * 普通++, 线程不安全
     */
    private void normalAdd(){
        inc ++; //非原子操作
    }

    /**
     * 加锁 ++, 线程安全
     */
    private void lockAndAdd(){
        lock.lock();
        try {
            inc ++;
        }finally {
            lock.unlock();
        }
    }

    /**
     * synchronized 修饰代码块 ++, 线程安全
     */
    private synchronized void synchronizedAdd(){
        inc ++;
    }

    /**
     * 原子类 自增, 线程安全
     */
    private void atomicAdd(){
        atomicInc.getAndIncrement();
    }
}
