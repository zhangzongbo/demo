package com.example.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzongbo
 * @date 19-12-4 下午7:04
 */

@Slf4j
public class ClassLoaderTest {

    public static void main(String[] args) {

        List<Integer> integerList = new ArrayList<>(8);
        List<String> stringList = new ArrayList<>(8);

        log.info("loader: {}", integerList.getClass().getClassLoader());
        log.info("loader: {}", stringList.getClass().getClassLoader());


        log.info("是否相等: {}", integerList.getClass() == stringList.getClass());

    }
}
