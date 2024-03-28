package com.korant.youya.workplace.pojo.vo.huntjob;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName HuntJobSharedVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/28 20:04
 * @Version 1.0
 */
@Data
public class HuntJobSharedVo {

    private Long fromUserId;

    private LocalDateTime timestamp;  // createdTime
}
