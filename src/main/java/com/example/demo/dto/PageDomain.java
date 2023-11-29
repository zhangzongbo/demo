package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * @author zhangzongbo
 * created on 2023-07-07 15:17
 */

@Data
public class PageDomain {

    @Min(value = 1, message = "分页大小不能小于1")
    @Max(value = 200, message = "分页大小不能大于200")
    @NotNull(message = "分页大小不能为空")
    @ApiModelProperty("分页大小")
    private long pageSize;

    @NotNull(message = "页数不能为空")
    @Min(value = 1, message = "页数不能小于1")
    @ApiModelProperty("页数")
    private long pageNum;

}
