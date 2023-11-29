package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zhangzongbo
 * created on 2023-07-07 16:04
 */

@Data
public class OrderDTO {

    @ApiModelProperty(value = "编码")
    private String orderId;

    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户编码")
    private String userCode;

    @ApiModelProperty(value = "订单总费用")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "物流单号")
    private String logisticsOrder;

    @ApiModelProperty(value = "付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "收货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveTime;

}
