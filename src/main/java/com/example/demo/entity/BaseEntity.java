package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhangzongbo
 * created on 2023-01-16 14:14
 */

@Data
public class BaseEntity {

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * extend field
     */
    private String extendField;

}
