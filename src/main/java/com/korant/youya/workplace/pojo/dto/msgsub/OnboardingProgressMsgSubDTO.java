package com.korant.youya.workplace.pojo.dto.msgsub;

import lombok.Data;
import lombok.experimental.Accessors;

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
     * 职位 ID
     */
    private Long jobId;

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
