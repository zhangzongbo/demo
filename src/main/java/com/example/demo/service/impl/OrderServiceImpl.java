package com.example.demo.service.impl;

import com.example.demo.dto.OrderRequest;
import com.example.demo.entity.Order;
import com.example.demo.enums.OrderStatusChangeEvent;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.mapper.dao.OrderDAO;
import com.example.demo.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangzongbo
 * created on 2023-01-16 17:44
 */

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {


    @Resource
    private OrderDAO orderDAO;

    @Resource
    private StateMachine<OrderStatusEnum, OrderStatusChangeEvent> orderStateMachine;
    @Resource
    private StateMachinePersister<OrderStatusEnum, OrderStatusChangeEvent, Order> stateMachinePseudoPersister;

    @Override
    public List<Order> list(OrderRequest request) {


        return orderDAO.getListByRequest(request);

    }

    @Override
    public String createOrder() {
        return "";
    }

    @Override
    public int payOrder(String orderId) {

        Order order = orderDAO.getByOrderId(orderId);
        log.info("线程名称：{},尝试支付，订单号：{}" ,Thread.currentThread().getName() , orderId);
        if (!sendEvent(OrderStatusChangeEvent.PAY, order)) {
            log.error("线程名称：{},支付失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("支付失败, 订单状态异常");
        }

        return 0;
    }

    @Override
    public int send(String orderId) {

        Order order = orderDAO.getByOrderId(orderId);
        log.info("线程名称：{},尝试发货，订单号：{}" ,Thread.currentThread().getName() , orderId);
        if (!sendEvent(OrderStatusChangeEvent.SEND, order)) {
            log.error("线程名称：{}, 发货失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("发货失败, 订单状态异常");
        }

        return 0;
    }

    @Override
    public int receive(String orderId) {

        Order order = orderDAO.getByOrderId(orderId);
        log.info("线程名称：{},尝试收货，订单号：{}" ,Thread.currentThread().getName() , orderId);
        if (!sendEvent(OrderStatusChangeEvent.RECEIVE, order)) {
            log.error("线程名称：{}, 收货失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("收货失败, 订单状态异常");
        }

        return 0;
    }

    @Override
    public int cancel(String orderId) {

        Order order = orderDAO.getByOrderId(orderId);
        log.info("线程名称：{},尝试取消，订单号：{}" ,Thread.currentThread().getName() , orderId);
        if (!sendEvent(OrderStatusChangeEvent.CANCEL, order)) {
            log.error("线程名称：{}, 取消失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("取消失败, 订单状态异常");
        }

        return 0;
    }


    /**
     * 发送订单状态转换事件
     * synchronized修饰保证这个方法是线程安全的
     *
     * @param changeEvent
     * @param order
     * @return
     */
    private synchronized boolean sendEvent(OrderStatusChangeEvent changeEvent, Order order) {
        boolean result = false;
        try {
            //启动状态机
            orderStateMachine.start();
            //尝试恢复状态机状态
            stateMachinePseudoPersister.restore(orderStateMachine, order);
            Message<OrderStatusChangeEvent> message = MessageBuilder.withPayload(changeEvent).setHeader("order", order).build();
            result = orderStateMachine.sendEvent(message);
            //持久化状态机状态
            stateMachinePseudoPersister.persist(orderStateMachine, order);
        } catch (Exception e) {
            log.error("订单操作失败:{}", e.getMessage(),  e);
        } finally {
            orderStateMachine.stop();
        }
        return result;
    }
}
