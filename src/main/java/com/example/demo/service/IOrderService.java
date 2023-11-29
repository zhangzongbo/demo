package com.example.demo.service;

import com.example.demo.dto.OrderRequest;
import com.example.demo.entity.Order;

import java.util.List;

/**
 * @author zhangzongbo
 * created on 2023-01-16 17:40
 */

public interface IOrderService {

    List<Order> list(OrderRequest request);

    String createOrder();

    int payOrder(String orderId);

    int send(String orderId);

    int receive(String orderId);

    int cancel(String orderId);
}
