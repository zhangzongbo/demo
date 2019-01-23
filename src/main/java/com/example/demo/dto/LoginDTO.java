package com.example.demo.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author zhangzongbo
 * @date 19-1-14 下午6:16
 */

@Data
public class LoginDTO {

    /**
     * 用户名
     */
    @NotNull(message = "userName不能为空")
    @Length(min = 3,max = 7,message = "用户名长度应在3-7之间")
    String userName;

    /**
     * 密码
     */

    @NotNull(message = "password不能为空")
    @Length(min = 6,max = 20,message = "密码长度应在6-20之间")
    String password;

}
