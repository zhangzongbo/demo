package com.example.demo.config;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.Order;
import com.example.demo.enums.OrderStatusChangeEvent;
import com.example.demo.enums.OrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 状态机持久化
 *
 * @author zhangzongbo
 * created on 2023-07-10 14:06
 */

@Configuration
@Slf4j
public class OrderStatusMachinePersister<E, S> {

    /**
     * 持久化到内存map中
     *
     * @return
     */
    @Bean(name = "stateMachineMemPersister")
    public static StateMachinePersister getPersister() {
        return new DefaultStateMachinePersister(new StateMachinePersist() {
            @Override
            public void write(StateMachineContext context, Object contextObj) throws Exception {
                log.info("持久化状态机,context:{},contextObj:{}", JSON.toJSONString(context), JSON.toJSONString(contextObj));
                map.put(contextObj, context);
            }
            @Override
            public StateMachineContext read(Object contextObj) throws Exception {
                log.info("获取状态机,contextObj:{}", JSON.toJSONString(contextObj));
                StateMachineContext stateMachineContext = (StateMachineContext) map.get(contextObj);
                log.info("获取状态机结果,stateMachineContext:{}", JSON.toJSONString(stateMachineContext));
                return stateMachineContext;
            }
            private Map<Object, StateMachineContext> map = new HashMap<>();
        });
    }

    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    /**
     * 持久化到redis中，在分布式系统中使用
     *
     * @return
     */
    @Bean(name = "stateMachineRedisPersister")
    public RedisStateMachinePersister<E, S> getRedisPersister() {
        RedisStateMachineContextRepository<E, S> repository = new RedisStateMachineContextRepository<>(redisConnectionFactory);
        RepositoryStateMachinePersist p = new RepositoryStateMachinePersist<>(repository);
        return new RedisStateMachinePersister<>(p);
    }

    /**
     * 伪持久化
     *
     * @return stateMachineContext
     */
    @Bean(name = "stateMachinePseudoPersister")
    public StateMachinePersister<OrderStatusEnum, OrderStatusChangeEvent, Order> getPseudoPersister() {
        return new DefaultStateMachinePersister<>(new StateMachinePersist<OrderStatusEnum, OrderStatusChangeEvent, Order>() {
            @Override
            public void write(StateMachineContext<OrderStatusEnum, OrderStatusChangeEvent> context, Order contextObj) {
                // Do nothing
            }
            @Override
            public StateMachineContext<OrderStatusEnum, OrderStatusChangeEvent> read(Order order) {
                log.info("获取状态机,contextObj:{}", JSON.toJSONString(order));
                StateMachineContext<OrderStatusEnum, OrderStatusChangeEvent> stateMachineContext =
                        new DefaultStateMachineContext<>(OrderStatusEnum.getByValue(order.getOrderStatus()), null, null, null, null, "orderStateMachine");
                log.info("获取状态机结果,stateMachineContext:{}", JSON.toJSONString(stateMachineContext));
                return stateMachineContext;
            }
        });
    }
}
