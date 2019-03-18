package com.example.demo.task.delayTask;

import com.example.demo.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangzongbo
 * @date 19-3-18 下午3:01
 */

@Slf4j
@Component
@Order(value = 1)
public class DelayTaskConsumer {
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private Jedis jedis = new RedisClient().getJedisPool().getResource();

    @PostConstruct
    public void start(){
        log.info("消费者启动===========>>");
        scheduledExecutorService.scheduleWithFixedDelay(new DelayTaskHandler(),1,1, TimeUnit.SECONDS);
    }
    public class DelayTaskHandler implements Runnable{
        @Override
        public void run() {
            try {
                Set ids = jedis.zrangeByScore(Constants.DELAY_TASK_QUEUE, 0, System.currentTimeMillis(),0, 1);
                if(ids==null||ids.isEmpty()){
                    return;
                }
                log.info("待处理任务: {} 个", ids.size());
                for(Object id:ids){
                    Long count = jedis.zrem(Constants.DELAY_TASK_QUEUE, (String) id);
                    if(count!=null&&count==1){
                        log.info("消费任务。id - {} , time - {} , threadName - {}",id, DateTimeUtil.parseTimeFromStamp(System.currentTimeMillis()),Thread.currentThread().getName());
                    }
                }
            }catch (Exception e){
                log.error("延时队列 消费错误 {}", e.getMessage(), e);
            }
        }
    }
}
