package com.example.demo.task.delaytask;

import com.example.demo.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangzongbo
 * @date 19-3-18 下午3:01
 *
 * 主线程负责轮询 取任务 线程池 负责 多线程执行 任务
 */

@Slf4j
//@Component
public class DelayTaskConsumer {
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private ExecutorService threadPool = Executors.newFixedThreadPool(20);



    @PostConstruct
    public void start(){
        log.info("消费者启动===========>>");
        scheduledExecutorService.scheduleWithFixedDelay(new DelayTaskHandler(),1,100, TimeUnit.MILLISECONDS);
    }
    public class DelayTaskHandler implements Runnable{
        @Override
        public void run() {
            try {

                Jedis jedis = new RedisClient().getJedisPool().getResource();
                Set ids = jedis.zrangeByScore(Constants.DELAY_TASK_QUEUE, 0, System.currentTimeMillis(),0, 30);

                if(ids==null||ids.isEmpty()){
                    log.debug("当前没有任务");
                    return;
                }

                log.info("待处理任务: {} 个",ids.size());
                for(Object id:ids){

                    /* 多线程执行 */

                    threadPool.execute(() -> {
                        Jedis newJedis = new RedisClient().getJedisPool().getResource();
                        Long count = newJedis.zrem(Constants.DELAY_TASK_QUEUE, (String) id);
//                        newJedis.close();
                        if(count != null && count == 1){
                            try {
                                Thread.sleep(120);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }finally {
                                newJedis.close();
                            }
                            log.info("消费任务。id - {} , time - {} , threadName - {}",id, DateTimeUtil.parseTimeFromStamp(System.currentTimeMillis()),Thread.currentThread().getName());
                        }
                    });
                }
            }catch (Exception e){
                log.error("延时队列 消费错误 {}", e.getMessage(), e);
            }
        }
    }
}
