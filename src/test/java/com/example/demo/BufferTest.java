package com.example.demo;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author zhangzongbo
 * @date 19-1-15 上午11:54
 */
public class BufferTest {

    public static void main(String[] args) {
        int size = 1024;
        StringBuilder stringBuilder = new StringBuilder();

        File file = new File("/home/zhangzongbo/var/txt0d13eb378292b3f5c8374c291362906d.json");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            char[] charBuffer = new char[size];
            int bytesRead;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
            System.out.println(stringBuilder);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取流
     *
     * @param inStream
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }
}
