package com.example.demo;


import com.example.demo.util.MD5Utils;

/**
 * @author zhangzongbo
 * @date 19-2-20 下午4:53
 */
public class MD5Test {
    public static void main(String[] args) {
        String s = "password";
        System.out.println(MD5Utils.encode(s));
    }
}
