package com.example.demo.task.delaytask;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;


/**
 * @author zhangzongbo
 * @date 19-3-18 下午4:19
 */

@Slf4j
public class DelayTaskTest {

    public static void main(String[] args) {
        DelayTaskProducer producer = new DelayTaskProducer();
        long now = System.currentTimeMillis();
        log.info("start:{}",now);
        for (int i = 1; i < 100; i++ ){
//            long time = System.currentTimeMillis() + 3 * i * 1000L;
            long expireTime = now;
            producer.produce(String.valueOf(i) + "-" + UUID.randomUUID(),now);
        }

//        for (int j = 0; j < 10; j++){
//            new DelayTaskConsumer().start();
//        }

    }
}
