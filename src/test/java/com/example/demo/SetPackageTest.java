package com.example.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangzongbo
 * @date 19-11-19 下午2:04
 */

@Slf4j
public class SetPackageTest {

    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();

        HashMap<String, String> hashMap = new HashMap<>(4, 2);

        hashMap.put("0x01", "1");
        hashMap.put("0x02", "2");
        hashMap.put("0x03", "3");
        hashMap.put("0x04", "4");
        hashMap.put("0x05", "5");
        hashMap.put("0x06", "6");
        hashMap.put("0x07", "7");
        String old = hashMap.put("0x01", "2");
        log.info("old: {}", old);

        /* 实例化时指定accessOrder = true */
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>(16, 0.75f, true);
        linkedHashMap.put("a", "a");
        linkedHashMap.put("b", "b");
        linkedHashMap.put("c", "c");

        log.info("after int: {}", linkedHashMap);

        linkedHashMap.get("a");

        log.info("after get: {}", linkedHashMap);
        linkedHashMap.put("d", "d");
        log.info("after put: {}", linkedHashMap);
//        int thr = 3; //1010
//        thr = thr << 1; //100 --> 1000
//        log.info("thr: {}", thr);

        TreeMap<String, String> treeMap = new TreeMap<>();

        /* put get  等方法都加了synchronized 修饰 */
        Map<String, String> synchronizedHashMap = Collections.synchronizedMap(new HashMap<String, String>());

        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();

        ArrayList<Integer> list = new ArrayList<>(16);

        list.add(1);
        list.add(2);
        list.add(2);
        list.add(2);
        list.add(7);
        list.add(8);

//
//        for (Integer n : list){
//            if (n == 2){
//                log.info("待删除: {}", n);
//                list.remove(n);
//                log.info("当前长度: {}", list.size());
//            }
//
//        }

        Iterator iterator = list.iterator();
        while (iterator.hasNext()){
            Integer n = (Integer) iterator.next();
            if (n == 2){
                iterator.remove();
            }
        }

        log.info("after remove: {}", list);


    }

}
