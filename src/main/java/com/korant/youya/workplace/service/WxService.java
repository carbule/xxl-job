package com.korant.youya.workplace.service;

import com.korant.youya.workplace.pojo.dto.msgsub.InterviewAppointmentMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.msgsub.InterviewMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.msgsub.OnboardingMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.msgsub.OnboardingProgressMsgSubDTO;

/**
 * <p>
 * 微信对接服务
 * </p>
 *
 * @author zhouzhifeng
 * @since 2024/3/13 13:52
 */
public interface WxService {

    /**
     * 面试预约通知
     *
     * @param toUser    接收人
     * @param msgSubDTO 消息模板值
     */
    void sendInterviewAppointmentMessageSubscribe(String toUser, InterviewAppointmentMsgSubDTO msgSubDTO);

    /**
     * 面试通知
     *
     * @param toUser    接收人
     * @param msgSubDTO 消息模板值
     */
    void sendInterviewMessageSubscribe(String toUser, InterviewMsgSubDTO msgSubDTO);

    /**
     * 入职通知
     *
     * @param toUser    接收人
     * @param msgSubDTO 消息模板值
     */
    void sendOnboardingMessageSubscribe(String toUser, OnboardingMsgSubDTO msgSubDTO);

    /**
     * 入职进度通知
     *
     * @param toUser    接收人
     * @param msgSubDTO 消息模板值
     */
    void sendOnboardingProgressMessageSubscribe(String toUser, OnboardingProgressMsgSubDTO msgSubDTO);
}
