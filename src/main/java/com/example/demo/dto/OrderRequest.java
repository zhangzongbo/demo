package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *@author zhangzongbo
 *created on 2023-07-07 15:12
*/

@Data
public class OrderRequest extends PageDomain{

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单创建时间")
    private LocalDateTime createTime;

}
