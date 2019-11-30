package com.example.demo;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ConcurrentTest {

    public static void main(String[] args) {

        ExecutorService pool = Executors.newCachedThreadPool();
    }
}
