package com.example.demo.task.delaytask;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangzongbo
 * @date 19-3-19 下午2:52
 */

@Slf4j
public class RedisDelayQueueConsumer {

    private RedisDelayQueueConsumer(){

    }

    JedisPool jedisPool = new RedisClient().getJedisPool();

    private RedisConcurrentDelayQueue delayQueue = DelayQueueFactory.getRedisConcurrentDelayQueue("testdisqueue", 1000, false);

    /**
     * 单例
     */
    private static  class LazyHolder{
        private static  RedisDelayQueueConsumer redisDelayQueueConsumer = new RedisDelayQueueConsumer();
    }
    public static RedisDelayQueueConsumer getInstance(){
        return LazyHolder.redisDelayQueueConsumer;
    }

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(20);

    private void start(){
        log.info("消费者启动========>>>");
        scheduledExecutorService.scheduleWithFixedDelay(new handler(),1,1, TimeUnit.MILLISECONDS);
    }


    private class handler implements Runnable{

        @Override
        public void run() {
            try {
                log.info("running-----");
                DelayMessage message = delayQueue.pop();
                if (message == null){
                    log.info("当前没有任务-----");
                    return;
                }
                log.info("newssage : {} ",message);
                delayQueue.ack(message.getTmpKey());
            }catch (Exception e){
                log.error("executor Error! {}",e.getMessage(),e);
            }
        }
    }

    public static void main(String[] args) {
        RedisDelayQueueConsumer consumer = RedisDelayQueueConsumer.getInstance();
       consumer.start();

    }

}
