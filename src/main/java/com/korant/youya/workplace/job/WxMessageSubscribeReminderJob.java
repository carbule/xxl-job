package com.korant.youya.workplace.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.korant.youya.workplace.enums.AcceptanceStatusEnum;
import com.korant.youya.workplace.enums.CompletionStatusEnum;
import com.korant.youya.workplace.mapper.InterviewMapper;
import com.korant.youya.workplace.pojo.po.Interview;
import com.korant.youya.workplace.service.WxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 微信消息订阅提醒
 *
 * @author zhouzhifeng
 * @since 2024/3/26 14:20
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WxMessageSubscribeReminderJob {

    private final InterviewMapper interviewMapper;

    /**
     * 发送面试预约消息订阅
     */
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void sendInterviewAppointmentMessageSubscribe() {
        // 面试邀约创建超过一天没接受
        List<Interview> interviews = interviewMapper.selectList(new LambdaQueryWrapper<Interview>()
                .eq(Interview::getAcceptanceStatus, AcceptanceStatusEnum.PENDING.getStatus())
                .eq(Interview::getCompletionStatus, CompletionStatusEnum.INCOMPLETE.getStatus())
                .lt(Interview::getCreateTime, LocalDateTime.now().minusDays(1)));


    }
}
