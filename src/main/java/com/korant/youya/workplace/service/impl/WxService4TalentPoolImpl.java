package com.korant.youya.workplace.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.alibaba.fastjson.JSON;
import com.korant.youya.workplace.config.WxMaProperties;
import com.korant.youya.workplace.pojo.dto.msgsub.InterviewAppointmentMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.msgsub.InterviewMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.msgsub.OnboardingMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.msgsub.OnboardingProgressMsgSubDTO;
import com.korant.youya.workplace.service.WxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 微信服务 - 人才库相关
 * </p>
 *
 * @author zhouzhifeng
 * @since 2024/3/13 13:56
 */
@Service("wxService4TalentPoolImpl")
@RequiredArgsConstructor
@Slf4j
public class WxService4TalentPoolImpl implements WxService {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final WxMaService wxMaService;

    private final WxMaProperties wxMaProperties;

    @Override
    @Async
    public void sendInterviewAppointmentMessageSubscribe(String toUser, InterviewAppointmentMsgSubDTO msgSubDTO) {
        try {
            wxMaService.getSubscribeService()
                    .sendSubscribeMsg(new WxMaSubscribeMessage()
                            .setToUser(toUser)
                            .setTemplateId(wxMaProperties.getMessageSubscribeTemplate().getInterviewAppointment())
                            .setMiniprogramState(wxMaProperties.getMessageSubscribeTemplate().getMiniprogramState())
                            .setPage(String.format("subpackages/PackageUser/xapply/share-steps?id=%s", msgSubDTO.getInternalRecommendId()))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("thing1").setValue(msgSubDTO.getPositionName()))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("time2").setValue(msgSubDTO.getFinalTime().format(FORMATTER)))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("thing3").setValue(msgSubDTO.getTips())));
        } catch (Exception e) {
            log.error("发送微信消息订阅失败：toUser={}, msgSubDTO={}", toUser, JSON.toJSONString(msgSubDTO));
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendInterviewMessageSubscribe(String toUser, InterviewMsgSubDTO msgSubDTO) {
        try {
            wxMaService.getSubscribeService()
                    .sendSubscribeMsg(new WxMaSubscribeMessage()
                            .setToUser(toUser)
                            .setTemplateId(wxMaProperties.getMessageSubscribeTemplate().getInterview())
                            .setMiniprogramState(wxMaProperties.getMessageSubscribeTemplate().getMiniprogramState())
                            .setPage(String.format("subpackages/PackageUser/xapply/share-steps?id=%s", msgSubDTO.getInternalRecommendId()))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("thing1").setValue(msgSubDTO.getPositionName()))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("thing4").setValue(msgSubDTO.getEnterpriseName()))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("time3").setValue(msgSubDTO.getTime().format(FORMATTER)))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("thing6").setValue(msgSubDTO.getLinkman())));
        } catch (Exception e) {
            log.error("发送微信消息订阅失败：toUser={}, msgSubDTO={}", toUser, JSON.toJSONString(msgSubDTO));
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendOnboardingMessageSubscribe(String toUser, OnboardingMsgSubDTO msgSubDTO) {
        try {
            wxMaService.getSubscribeService()
                    .sendSubscribeMsg(new WxMaSubscribeMessage()
                            .setToUser(toUser)
                            .setTemplateId(wxMaProperties.getMessageSubscribeTemplate().getOnboarding())
                            .setMiniprogramState(wxMaProperties.getMessageSubscribeTemplate().getMiniprogramState())
                            .setPage(String.format("subpackages/PackageUser/xapply/share-steps?id=%s", msgSubDTO.getInternalRecommendId()))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("thing3").setValue(msgSubDTO.getPositionName()))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("thing2").setValue(msgSubDTO.getEnterpriseName()))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("time6").setValue(msgSubDTO.getTime().format(FORMATTER))));
        } catch (Exception e) {
            log.error("发送微信消息订阅失败：toUser={}, msgSubDTO={}", toUser, JSON.toJSONString(msgSubDTO));
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendOnboardingProgressMessageSubscribe(String toUser, OnboardingProgressMsgSubDTO msgSubDTO) {
        try {
            wxMaService.getSubscribeService()
                    .sendSubscribeMsg(new WxMaSubscribeMessage()
                            .setToUser(toUser)
                            .setTemplateId(wxMaProperties.getMessageSubscribeTemplate().getOnboardingProgress())
                            .setMiniprogramState(wxMaProperties.getMessageSubscribeTemplate().getMiniprogramState())
                            .setPage(String.format("subpackages/PackageUser/xapply/share-steps?id=%s", msgSubDTO.getInternalRecommendId()))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("thing4").setValue(msgSubDTO.getPositionName()))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("thing3").setValue(msgSubDTO.getEnterpriseName()))
                            .addData(new WxMaSubscribeMessage.MsgData().setName("thing5").setValue(msgSubDTO.getProgress())));
        } catch (Exception e) {
            log.error("发送微信消息订阅失败：toUser={}, msgSubDTO={}", toUser, JSON.toJSONString(msgSubDTO));
            log.error(e.getMessage(), e);
        }
    }
}
