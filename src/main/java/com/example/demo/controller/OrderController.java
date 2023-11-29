package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderRequest;
import com.example.demo.entity.JSONResult;
import com.example.demo.entity.Order;
import com.example.demo.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzongbo
 * created on 2023-01-16 14:12
 */
@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("getList")
    public JSONResult<List<OrderDTO>> getList(@Valid OrderRequest request){

        List<OrderDTO> result = new ArrayList<>();

        List<Order> orderList = orderService.list(request);

        orderList.forEach(o -> {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(o, orderDTO);
            result.add(orderDTO);
        });
        return JSONResult.ok(result);
    }

    /**
     * 创建订单
     * @return order id
     */
    public JSONResult<String> createOrder(){
        return JSONResult.ok(orderService.createOrder());
    }

    /**
     * 付款
     * @return
     */

    @GetMapping("pay")
    public JSONResult<Boolean> pay(){

        orderService.payOrder("1234");
        return JSONResult.ok();
    }

    /**
     * 发货
     * @return
     */

    @GetMapping("send")
    public JSONResult<Boolean> send(@RequestParam("orderId") String orderId){
        orderService.send(orderId);

        return JSONResult.ok();
    }

    /**
     * 收货
     * @return
     */
    @GetMapping("receive")
    public JSONResult<Boolean> receive(@RequestParam("orderId") String orderId){
        orderService.receive(orderId);

        return JSONResult.ok();
    }

    /**
     * 取消订单
     * @return
     */
    @GetMapping("cancel")
    public JSONResult<Boolean> cancel(@RequestParam("orderId") String orderId){
        orderService.cancel(orderId);

        return JSONResult.ok();
    }

    /**
     * 订单列表
     * @return
     */
    public JSONResult<List<Order>> orderList(){
        return null;
    }


}
