package com.korant.youya.workplace.pojo.dto.msgsub;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 面试通知
 *
 * @author zhouzhifeng
 * @since 2024/3/26 9:57
 */
@Data
@Accessors(chain = true)
public class InterviewMsgSubDTO {

    /**
     * 职位 ID
     */
    private Long jobId;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 公司名称
     */
    private String enterpriseName;

    /**
     * 面试时间
     */
    private LocalDateTime time;

    /**
     * 联系人
     */
    private String linkman;
}
