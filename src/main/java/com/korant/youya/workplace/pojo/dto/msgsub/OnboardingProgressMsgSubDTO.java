package com.korant.youya.workplace.pojo.dto.msgsub;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 入职进度通知
 *
 * @author zhouzhifeng
 * @since 2024/3/26 9:59
 */
@Data
@Accessors(chain = true)
public class OnboardingProgressMsgSubDTO {

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 用工单位
     */
    private String enterpriseName;

    /**
     * 入职进度
     */
    private String progress;
}
