package com.example.demo.config;

import com.example.demo.enums.OrderStatusChangeEvent;
import com.example.demo.enums.OrderStatusEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

/**
 * 订单状态机 配置类
 *
 * @author zhangzongbo
 * created on 2023-07-10 13:59
 */

@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStatusEnum, OrderStatusChangeEvent> {

    /**
     * 配置状态
     *
     * @param states
     * @throws Exception
     */

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatusEnum, OrderStatusChangeEvent> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatusEnum.WAIT_PAYMENT)
                .states(EnumSet.allOf(OrderStatusEnum.class));
    }
    /**
     * 配置状态转换事件关系
     *
     * @param transitions
     * @throws Exception
     */

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatusEnum, OrderStatusChangeEvent> transitions) throws Exception {
        transitions
                //支付事件:待支付 -> 待发货
                .withExternal().source(OrderStatusEnum.WAIT_PAYMENT).target(OrderStatusEnum.WAIT_SEND).event(OrderStatusChangeEvent.PAY)
                .and()
                //发货事件:待发货 -> 待收货
                .withExternal().source(OrderStatusEnum.WAIT_SEND).target(OrderStatusEnum.SENDING).event(OrderStatusChangeEvent.SEND)
                .and()
                //收货事件:待收货 -> 已完成
                .withExternal().source(OrderStatusEnum.SENDING).target(OrderStatusEnum.RECEIVED).event(OrderStatusChangeEvent.RECEIVE);
    }

}
