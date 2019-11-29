package com.example.demo;

import lombok.extern.slf4j.Slf4j;



/**
 * @author zhangzongbo
 * @date 19-11-28 下午2:24
 */

@Slf4j
public class SingleTonTest {

    private static volatile SingleTonTest ton;
    private SingleTonTest() {}

    public SingleTonTest getTon(){

        if (ton == null){
            synchronized (this){
                if (ton == null){
                    ton = new SingleTonTest();
                }
            }
        }
        return ton;
    }

    public static void main(String[] args) {

        for (int i = 10; i < 50; i++) {

            new Thread(() -> {
                SingleTonTest ton = new SingleTonTest().getTon();
                log.info("hashcode: {}", ton.hashCode());
            }).start();

        }

    }
}
