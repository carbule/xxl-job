package com.korant.youya.workplace.pojo.vo.confirmation;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName ConfirmationVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/9 14:30
 * @Version 1.0
 */
@Data
public class ConfirmationVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 转正时间
     */
    private LocalDateTime confirmationTime;

    /**
     * 转正薪资
     */
    private Integer salary;

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
