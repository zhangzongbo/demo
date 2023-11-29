package com.example.demo.enums;

import java.util.Objects;

/**
 * @author zhangzongbo
 * created on 2023-07-10 12:01
 */
public enum OrderStatusEnum {

    WAIT_PAYMENT(1, "待支付"),
    WAIT_SEND(2, "待发货"),
    SENDING(3, "送货中"),
    RECEIVED(4, "已收货"),

    ;


    private Integer value;

    private String desc;

    OrderStatusEnum(Integer value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderStatusEnum getByValue(Integer value){

        if (Objects.isNull(value)){
            return null;
        }
        for (OrderStatusEnum en : OrderStatusEnum.values()){
            if (Objects.equals(value, en.value)){
                return en;
            }
        }
        return null;
    }
}
