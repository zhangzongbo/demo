package com.example.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzongbo
 * @date 19-12-6 下午8:00
 */
public class StringOomMock {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        while (true){
            final String temp = "String";
            list.add(temp);
        }
    }
}
