package com.example.demo.proxy;

import java.lang.reflect.Proxy;

/**
 * @author zhangzongbo
 * @date 19-12-4 下午4:25
 */
public class HelloTest {

    public static void main(String[] args) {
        HelloImpl hello = new HelloImpl();

        HelloProxyHandler handler = new HelloProxyHandler(hello);

        HelloInterface helloProxy = (HelloInterface) Proxy.newProxyInstance(HelloInterface.class.getClassLoader(), new Class[]{HelloInterface.class}, handler);

        helloProxy.sayHello();

        try {
            for (int i = 0; i < Integer.MAX_VALUE; i++){
                HelloProxyCgLib hpcg = new HelloProxyCgLib();

                HelloImpl helloCglib = (HelloImpl) hpcg.getProxy(HelloImpl.class);
                helloCglib.sayHello();
            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }
}
