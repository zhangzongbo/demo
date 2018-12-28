package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhangzongbo
 * @date 18-12-26 下午4:57
 */

@Data
@AllArgsConstructor
public class DoPostDTO {

    @NotNull(message = "name不能为空")
    private String name;

    @NotNull(message = "id不能为空")
    private Integer id;

    private String nano;
}
