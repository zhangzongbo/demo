package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zhangzongbo
 * created on 2023-01-16 14:13
 */

@Data()
@TableName("t_order")
public class Order extends BaseEntity{

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 编码
     */
    private String orderId;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户编码
     */
    private String userCode;


    /**
     * 订单总费用
     */
    private BigDecimal totalFee;

    /**
     * 物流单号
     */
    private String logisticsOrder;

    /**
     * 付款时间
     */
    private LocalDateTime payTime;

    /**
     * 发货时间
     */
    private LocalDateTime sendTime;

    /**
     * 收货时间
     */
    private LocalDateTime receiveTime;

}
