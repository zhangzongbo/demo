package com.example.demo;

/**
 * @author zhangzongbo
 * @date 19-12-6 下午8:59
 */


import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

public class MetaSpaceOom {

    public static void main(String[] args) throws Exception {
        System.out.println("MetaSpaceOom");
        String expression = "(1+2+3)/2";
        int num = 1000000;
        GroovyClassLoader loader = new GroovyClassLoader();

        for (int i = 0; i < num; i++) {
            try {
                // loader = new GroovyClassLoader();
                // 表达式不一样才会生成新的class
                expression = "a+" + i;
                // 编译执行
                Class groovyClass = loader.parseClass(expression);

                GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
                // groovyObject 不可缓存
                groovyObject.setProperty("a", 1);

                Object result = groovyObject.invokeMethod("run", new Object[] {});
                System.out.println(i + "," + result + "," + groovyClass.getSimpleName());

            } catch (Throwable e) {
//                System.out.println(e);
                e.printStackTrace();
                // 不加这个会一直 java.lang.OutOfMemoryError: Metaspace
                loader = new GroovyClassLoader();
                // System.gc();
                Thread.sleep(3000);
            }
        }
    }
}