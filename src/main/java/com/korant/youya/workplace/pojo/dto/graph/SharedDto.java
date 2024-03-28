package com.korant.youya.workplace.pojo.dto.graph;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName SharedDto
 * @Description // 用作数据转换，将关系数据库中的职位分享和求职分享
 * // 转换为Neo4j中的"from...to..."的关系
 * @Author chenyiqiang
 * @Date 2024/3/28 15:44
 * @Version 1.0
 */
@Data
public class SharedDto {

    private Long fromUserId;

    private Long toUserId;

    private Long targetId;

    private Boolean isHr;

    private LocalDateTime timestamp;  // createdTime
}
