package com.example.demo.util;


import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author zhangzongbo
 * @date 19-3-18 下午5:58
 */

@Slf4j
public class DateTimeUtil {

    private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

    public static SimpleDateFormat getSDF()
    {
        SimpleDateFormat sdf = threadLocal.get();
        if(sdf==null){
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            threadLocal.set(sdf);
        }
        return sdf;
    }


    public static String parseTimeFromStamp(long timestamp){
        SimpleDateFormat sdf = getSDF();
        try {
            return sdf.format(timestamp);
        } catch (Exception e) {
            log.error("时间格式化异常: {}",e.getMessage(), e);
        }
        return "时间错误";
    }

    public static void main(String[] args) {
        System.out.println(parseTimeFromStamp(System.currentTimeMillis()));
    }
}
