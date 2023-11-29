package com.example.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author zhangzongbo
 * created on 2023-02-08 16:08
 */
public class BigDecimalTest {

    public static void main(String[] args) {
        BigDecimal b = new BigDecimal("0.65");

        System.out.println(b.divide(new BigDecimal(1) , 1, RoundingMode.HALF_UP));


    }
}
