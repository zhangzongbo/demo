package com.example.demo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhangzongbo
 * @date 19-1-14 下午6:26
 */

@Data
@Configuration
@PropertySource("application.properties")
public class StaticConfig {

    @Value("${login.username}")
    private String userName;

    @Value("${login.password}")
    String password;
}
