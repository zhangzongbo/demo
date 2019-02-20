package com.example.demo;

/**
 * @author zhangzongbo
 * @date 19-1-25 下午3:46
 */
public class FieldTest {

    public static void main(String[] args) {
        /**
         * 位运算
         */

        int num = 2;
        print(num);
        num = num << 3;
        print(num);
        num = 1024;
        print(num);
    }


    private static void print(int num){
        int maxLength = 11;
        String rt = Integer.toBinaryString(num);
        if (rt.length() < maxLength){

        }
        System.out.println(Integer.toBinaryString(num));
    }
}
