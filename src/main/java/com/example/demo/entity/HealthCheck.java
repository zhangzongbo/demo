package com.example.demo.entity;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @author zhangzongbo
 * @date 18-12-26 上午11:18
 */
@Component
public class HealthCheck implements HealthIndicator {

    @Override
    public Health health() {
        // perform some specific health check
        int errorCode = check();
        if (errorCode != 0) {
            return Health.down()
                    .withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

    public int check() {
        // Our logic to check health
        return 0;
    }
}
