package com.example.demo.mapper.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.dto.OrderRequest;
import com.example.demo.entity.Order;
import com.example.demo.mapper.OrderMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Order DAO
 *
 * @author zhangzongbo
 * created on 2023-07-11 18:05
 */

@Component
public class OrderDAO {

    @Resource
    private OrderMapper orderMapper;


    /**
     * get order by orderId
     * @param orderId order id
     * @return Order DO
     */
    public Order getByOrderId(String orderId){

        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>()
                .eq(StringUtils.isNotBlank(orderId), "order_id", orderId);

        return orderMapper.selectOne(queryWrapper);
    }


    /**
     * get order list by order request
     * @param request query request
     * @return order list
     */
    public List<Order> getListByRequest(OrderRequest request){

        if (Objects.isNull(request)){
            return Collections.emptyList();
        }

        Page<Order> page = new Page<>(request.getPageNum(), request.getPageSize());

        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>()
                .eq(StringUtils.isNotBlank(request.getOrderId()), "order_id", request.getOrderId())
                .eq(Objects.nonNull(request.getOrderStatus()), "order_status", request.getOrderStatus())
                .gt(Objects.nonNull(request.getCreateTime()), "create_time", request.getCreateTime());

        return orderMapper.selectPage(page, queryWrapper).getRecords();

    }
 }
