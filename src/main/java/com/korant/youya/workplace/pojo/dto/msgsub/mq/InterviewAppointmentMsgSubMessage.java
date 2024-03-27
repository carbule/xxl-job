package com.korant.youya.workplace.pojo.dto.msgsub.mq;

import com.korant.youya.workplace.pojo.po.Interview;
import com.korant.youya.workplace.pojo.po.Job;
import com.korant.youya.workplace.pojo.po.User;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouzhifeng
 * @since 2024/3/27 9:19
 */
@Data
@Accessors
public class InterviewAppointmentMsgSubMessage {

    /**
     * 职位
     */
    private Job job;

    /**
     * 职位申请 ID
     */
    private Long applyJobId;

    /**
     * 内部推荐 ID
     */
    private Long internalRecommendId;

    /**
     * 面试人
     */
    private User user;

    /**
     * 面试记录
     */
    private Interview interview;
}
