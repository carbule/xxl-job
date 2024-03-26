package com.korant.youya.workplace.pojo.dto.msgsub;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 入职通知
 *
 * @author zhouzhifeng
 * @since 2024/3/26 9:59
 */
@Data
@Accessors(chain = true)
public class OnboardingMsgSubDTO {

    /**
     * 职位 ID
     */
    private Long jobId;

    /**
     * 入职职位
     */
    private String positionName;

    /**
     * 入职公司
     */
    private String enterpriseName;

    /**
     * 报到时间
     */
    private LocalDateTime time;
}
