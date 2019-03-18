package com.example.demo.task.delayTask;

import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;



/**
 * @author zhangzongbo
 * @date 19-3-18 下午3:10
 */
@Component
public class RedisClient {
    public JedisPool getJedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(8);
        config.setMinIdle(1);
        config.setMaxWaitMillis(5000);

        return new JedisPool(config);
    }


}
