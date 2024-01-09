package com.korant.youya.workplace.pojo.vo.interview;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName InterviewVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/9 11:05
 * @Version 1.0
 */
@Data
public class InterviewVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 面试方式
     */
    private Integer approach;

    /**
     * 面试时间
     */
    private LocalDateTime interTime;

    /**
     * 备注
     */
    private String note;

    /**
     * 接受状态 0-待接受 1-拒绝 2-接受
     */
    private Integer acceptanceStatus;

    /**
     * 完成状态 0-未完成 1-已取消 2-已完成
     */
    private Integer completionStatus;
}
