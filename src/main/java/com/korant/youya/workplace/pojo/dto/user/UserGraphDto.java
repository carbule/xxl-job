package com.korant.youya.workplace.pojo.dto.user;

import lombok.Data;

/**
 * @ClassName UserGraphDto
 * @Description 用于向Neo4j中同步用户数据
 * @Author chenyiqiang
 * @Date 2024/3/28 16:10
 * @Version 1.0
 */
@Data
public class UserGraphDto {

    private Long id;

    private String phone;
}
