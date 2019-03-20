package com.example.demo.task.delaytask;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangzongbo
 * @date 19-3-19 下午3:37
 */

@Slf4j
public class RedisDelayQueueProducer {
    public static void main(String[] args) {
        RedisConcurrentDelayQueue delayQueue = DelayQueueFactory.getRedisConcurrentDelayQueue("testdisqueue", 5000, true);
        for (int i = 0; i < 10; i++) {
            delayQueue.push(i + "--test");
        }

        log.info("produce end ============>>");
    }
}
