package com.example.demo.task.delayTask;

import com.example.demo.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;


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
        for (int i = 1; i < 50; i++ ){
//            long expireTime = System.currentTimeMillis() + 5 * i * 1000L;
            long expireTime = now;
            log.info("添加任务 - {} || 预计 {} 时执行", i, DateTimeUtil.parseTimeFromStamp(expireTime));
            producer.produce(String.valueOf(i),expireTime);
        }

//        try {
//            log.info("等待 1 秒");
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        for (int j = 0; j < 10; j++){
            new DelayTaskConsumer().start();
        }

    }
}
