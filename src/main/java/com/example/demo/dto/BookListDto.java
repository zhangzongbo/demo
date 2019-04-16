package com.example.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhangzongbo
 * @date 19-4-13 上午11:07
 */

@Data
public class BookListDto {


    private List<String> bookCodes;

    @NotNull(message = "filterBookCodes 不能为空")
    private List<String> filterBookCodes;
}
