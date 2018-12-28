package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhangzongbo
 * @date 18-12-21 下午2:35
 */

@Service
@Slf4j
public class AsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(200);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setQueueCapacity(20000);
        executor.setThreadNamePrefix("ASNYC-Thread-");
        executor.setAwaitTerminationSeconds(15 * 60);
        executor.setKeepAliveSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor myExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(200);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setQueueCapacity(20000);
        executor.setThreadNamePrefix("MY-ASNYC-");
        executor.setAwaitTerminationSeconds(15 * 60);
        executor.setKeepAliveSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomerExceptionHandler();
    }

    class CustomerExceptionHandler implements AsyncUncaughtExceptionHandler{

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            throwable.printStackTrace();
            log.error("Method Name: {} Exception message - {}",method.getName() ,throwable.getMessage());
            for (Object param : objects){
                log.info("Param value: {}",param);
            }
        }
    }
}
