package com.example.demo.entity;

import lombok.*;

/**
 * @author zhangzongbo
 * @date 18-12-26 上午10:21
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogWrapper {
    private String url;
    private String httpMethod;
    private String ip;
    private String classMethod;
    private String params;

    @Override
    public String toString(){
        return "REQUEST : {" +
                "\"URL\":\"" + url +
                "\", \"HTTP_METHOD\":\"" + httpMethod +
                "\", \"REMOTE_IP\":\"" + ip +
                "\", \"CLASS_METHOD\":\"" + classMethod +
                "\", \"PARAMS\":" + params +
                "}";
    }
}
