package com.example.demo.task.delaytask;

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
        config.setMaxIdle(10);
        config.setMinIdle(2);
        config.setMaxWaitMillis(5000);

        String host = "127.0.0.1";
        int port = 6379;
        return new JedisPool(config, host, port);
    }


}
