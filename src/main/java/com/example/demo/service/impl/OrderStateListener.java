package com.example.demo.service.impl;

import com.example.demo.entity.Order;
import com.example.demo.enums.OrderStatusChangeEvent;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhangzongbo
 * created on 2023-07-10 14:38
 */

@Component("orderStateListener")
@WithStateMachine(name = "orderStateMachine")
@Slf4j
public class OrderStateListener {

    @Resource
    private OrderMapper orderMapper;

    @OnTransition(source = "WAIT_PAYMENT", target = "WAIT_SEND")
    public void payTransition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        log.info("支付，状态机反馈信息：{}",  message.getHeaders().toString());
        //更新订单
        order.setOrderStatus(OrderStatusEnum.WAIT_SEND.getValue());
        orderMapper.updateById(order);
        //TODO 其他业务
    }


    @OnTransition(source = "WAIT_SEND", target = "SENDING")
    public void deliverTransition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        log.info("发货，状态机反馈信息：{}",  message.getHeaders().toString());
        //更新订单
        order.setOrderStatus(OrderStatusEnum.SENDING.getValue());
        orderMapper.updateById(order);
        //TODO 其他业务
    }


    @OnTransition(source = "SENDING", target = "RECEIVED")
    public void receiveTransition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        log.info("确认收货，状态机反馈信息：{}",  message.getHeaders().toString());
        //更新订单
        order.setOrderStatus(OrderStatusEnum.RECEIVED.getValue());
        orderMapper.updateById(order);
        //TODO 其他业务
    }
}
