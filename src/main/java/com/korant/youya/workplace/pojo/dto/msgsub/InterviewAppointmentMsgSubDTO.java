package com.korant.youya.workplace.pojo.dto.msgsub;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 面试预约通知
 *
 * @author zhouzhifeng
 * @since 2024/3/26 9:55
 */
@Data
@Accessors(chain = true)
public class InterviewAppointmentMsgSubDTO {

    /**
     * 职位 ID
     */
    private Long jobId;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 截止时间
     */
    private LocalDateTime finalTime;

    /**
     * 温馨提示
     */
    private String tips;
}
