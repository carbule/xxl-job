package com.korant.youya.workplace.listener;

import com.alibaba.fastjson2.JSON;
import com.korant.youya.workplace.constants.RabbitConstant;
import com.korant.youya.workplace.enums.AcceptanceStatusEnum;
import com.korant.youya.workplace.enums.CompletionStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.InterviewMapper;
import com.korant.youya.workplace.pojo.dto.msgsub.InterviewAppointmentMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.msgsub.mq.InterviewAppointmentMsgSubMessage;
import com.korant.youya.workplace.pojo.po.Interview;
import com.korant.youya.workplace.service.WxService;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * @author zhouzhifeng
 * @since 2024/3/26 17:49
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WxMessageSubscribeReminderListener {

    public static final String TIPS = "请在截止前完成预约，否则将视为放弃！";

    private final InterviewMapper interviewMapper;

    @Resource(name = "wxService4CandidateImpl")
    private WxService wxService4Candidate;

    @Resource(name = "wxService4TalentPoolImpl")
    private WxService wxService4TalentPool;

    /**
     * 面试预约通知-候选人
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = RabbitConstant.Exchange.WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_EXCHANGE, delayed = Exchange.TRUE),
            value = @Queue(name = RabbitConstant.Queue.WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_4CANDIDATE_QUEUE),
            key = RabbitConstant.RoutingKey.WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_4CANDIDATE_ROUTING_KEY
    ))
    @SneakyThrows
    public void sendInterviewAppointment4CandidateMessageSubscribe(Channel channel, Message msg) {
        String messageBodyString = new String(msg.getBody());
        log.info(">>> Queue: {}, Message body: {}", RabbitConstant.Queue.WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_4CANDIDATE_QUEUE, messageBodyString);
        InterviewAppointmentMsgSubMessage messageBody = JSON.parseObject(messageBodyString, InterviewAppointmentMsgSubMessage.class);

        try {
            Interview itr = Optional.ofNullable(interviewMapper.selectById(messageBody.getInterview().getId()))
                    .orElseThrow(() -> new YouyaException("找不到面试记录"));
            if (Objects.equals(itr.getAcceptanceStatus(), AcceptanceStatusEnum.PENDING.getStatus()) &&
                    Objects.equals(itr.getCompletionStatus(), CompletionStatusEnum.INCOMPLETE.getStatus())) {
                wxService4Candidate.sendInterviewAppointmentMessageSubscribe(messageBody.getUser().getWechatOpenId(), new InterviewAppointmentMsgSubDTO()
                        .setJobId(messageBody.getJob().getId())
                        .setApplyJobId(messageBody.getApplyJobId())
                        .setPositionName(messageBody.getJob().getPositionName())
                        .setFinalTime(messageBody.getInterview().getInterTime())
                        .setTips(TIPS));
            }
        } catch (Exception e) {
            log.error("发送面试预约消息订阅失败");
            log.error(e.getMessage(), e);
        } finally {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        }
    }

    /**
     * 面试预约通知-人才库
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = RabbitConstant.Exchange.WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_EXCHANGE, delayed = Exchange.TRUE),
            value = @Queue(name = RabbitConstant.Queue.WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_4TALENT_POOL_QUEUE),
            key = RabbitConstant.RoutingKey.WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_4TALENT_POOL_ROUTING_KEY
    ))
    @SneakyThrows
    public void sendInterviewAppointment4TalentPoolMessageSubscribe(Channel channel, Message msg) {
        String messageBodyString = new String(msg.getBody());
        log.info(">>> Queue: {}, Message body: {}", RabbitConstant.Queue.WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_4TALENT_POOL_QUEUE, messageBodyString);
        InterviewAppointmentMsgSubMessage messageBody = JSON.parseObject(new String(msg.getBody()), InterviewAppointmentMsgSubMessage.class);

        try {
            Interview itr = Optional.ofNullable(interviewMapper.selectById(messageBody.getInterview().getId()))
                    .orElseThrow(() -> new YouyaException("找不到面试记录"));
            if (Objects.equals(itr.getAcceptanceStatus(), AcceptanceStatusEnum.PENDING.getStatus()) &&
                    Objects.equals(itr.getCompletionStatus(), CompletionStatusEnum.INCOMPLETE.getStatus())) {
                wxService4TalentPool.sendInterviewAppointmentMessageSubscribe(messageBody.getUser().getWechatOpenId(), new InterviewAppointmentMsgSubDTO()
                        .setJobId(messageBody.getJob().getId())
                        .setInternalRecommendId(messageBody.getInternalRecommendId())
                        .setPositionName(messageBody.getJob().getPositionName())
                        .setFinalTime(messageBody.getInterview().getInterTime())
                        .setTips(TIPS));
            }
        } catch (Exception e) {
            log.error("发送面试预约消息订阅失败");
            log.error(e.getMessage(), e);
        } finally {
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
