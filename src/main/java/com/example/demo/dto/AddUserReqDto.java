package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;


/**
 * @author zhangzongbo
 * @date 19-2-20 上午10:38
 */

@Data
@NoArgsConstructor
public class AddUserReqDto {

    @NotNull(message = "用户名不能为空")
    @Length(min = 3, max = 15,message = "用户名长度应该在3-15之间")
    String userName;

    @NotNull(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度应在6-20之间")
    String password;
}
