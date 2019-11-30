package com.example.demo.leetcode;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author zhangzongbo
 * @date 19-11-27 下午8:06
 */

@Slf4j
public class SubStringMaxLength {

    public static void main(String[] args) {



    }

    /**
     *
     * 找出给定字符串的最长子串长度
     *
     * a b c b a b
     *
     * 3 --> a b c
     *
     * @param s
     */
    private void findMax(String s){

        HashMap<Character, Integer> map = new HashMap<>(16);
        int len = s.length(), ans = 0;
        for (int start = 0, end = 0; end < len; end ++){
            char c = s.charAt(start);

        }
    }
}
