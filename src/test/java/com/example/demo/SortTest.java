package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.Arrays;

/**
 * @author zhangzongbo
 * @date 19-12-6 下午2:54
 */

@Slf4j
public class SortTest {

    private void sysSort(){
        int[] array = {5, 9, 1, 9, 5, 3, 7, 6, 1};

        Arrays.sort(array);

        log.info("sys sort: {}", array);

    }

    /**
     * 冒泡排序, 双循环
     */
    private void popSort(){

        int[] array = {5, 9, 1, 9, 5, 3, 7, 6, 1};

        for (int i = 0; i < array.length; i++){
            for (int j = 0; j < array.length; j++){
                if (array[i] < array[j]){
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
        log.info("pop sort: {}", array);
    }

    /**
     * 快速排序: 分治递归
     */
    private void quickSort(int[] subArray, int begin, int end){

        if (begin < end){

            int key = subArray[begin];
            int i = begin;
            int j = end;

            while (i < j){
                while (i < j && subArray[j] > key ){
                    j--;
                }

                if (i < j){
                    subArray[i] = subArray[j];
                    i++;
                }

                while (i < j && subArray[i] < key){
                    i++;
                }

                if (i < j){
                    subArray[j] = subArray[i];
                    j--;
                }
                subArray[i] = key;

                /* 递归操作 */
                quickSort(subArray, begin, i - 1);
                quickSort(subArray, i + 1, end);
            }
        }


    }



    public static void main(String[] args) {

        SortTest sort = new SortTest();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("sysSort");
        sort.sysSort();
        stopWatch.stop();
        stopWatch.start("popSort");
        sort.popSort();
        stopWatch.stop();

        int[] array = {5, 9, 1, 9, 5, 3, 7, 6, 1};
        stopWatch.start("quickSort");
        sort.quickSort(array, 0, array.length - 1);
        log.info("quick sort: {}", array);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
    }
}
