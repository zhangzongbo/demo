package com.example.demo.entity.enums;

/**
 * @author zhangzongbo
 * created on 2023-01-16 14:35
 */


public enum OrderStatusEnum {


    INIT(0, "创建"),
    PAY_WAITING(1, "待支付"),
    SEND_WAITING(2, "待发货"),
    SENDING(3, "送货中"),
    RECEIVE(4,"已收货"),
    CANCEL(5, "已取消"),
    ;

    private Integer status;

    private String desc;

    OrderStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getDesc() {
        return this.desc;
    }
}
