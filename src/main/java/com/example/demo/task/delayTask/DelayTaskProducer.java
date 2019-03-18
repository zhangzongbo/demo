package com.example.demo.task.delayTask;

import com.example.demo.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * @author zhangzongbo
 * @date 19-3-18 下午3:01
 */

@Slf4j
@Service
public class DelayTaskProducer {
    private Jedis jedis = new RedisClient().getJedisPool().getResource();

    public void produce(String newId, long timeStamp){

        try {
            log.info("添加任务 - {} || 预计 {} 时执行", newId, DateTimeUtil.parseTimeFromStamp(timeStamp));
            jedis.zadd(Constants.DELAY_TASK_QUEUE, timeStamp, newId);
        }catch (Exception e){
            log.error("延时队列 生产异常 {}", e.getMessage(), e);
        }
    }
}
