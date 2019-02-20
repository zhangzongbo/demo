package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author zhangzongbo
 * @date 19-2-19 下午7:30
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResDto implements Serializable {
    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态 1启用 0 停用
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
