package com.example.demo.task.delaytask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import redis.clients.jedis.Jedis;

import java.nio.channels.NonWritableChannelException;
import java.util.UUID;

/**
 * @author zhangzongbo
 * @date 19-3-19 下午8:36
 */

@Slf4j
public class JedisTest {

    private static Jedis jedis = new RedisClient().getJedisPool().getResource();

    private static long now = System.currentTimeMillis();
    public static void main(String[] args) {

        String key = "HELLO_WORLD";
        for (int i = 0; i < 10; i ++){
            jedis.zadd(key, i * 1000 + now, "test-" + UUID.randomUUID());
        }
        jedis.close();
        log.info("test end");

    }
}
