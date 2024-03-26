package com.korant.youya.workplace.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Data
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMaProperties {

    private List<Config> configs;

    private MessageSubscribeTemplate messageSubscribeTemplate;

    @Data
    public static class Config {
        /**
         * 设置微信小程序的appid
         */
        private String appid;

        /**
         * 设置微信小程序的Secret
         */
        private String secret;
    }

    @Data
    public static class MessageSubscribeTemplate {

        /**
         * 跳转小程序类型
         */
        private String miniprogramState;

        /**
         * 面试预约通知
         */
        private String interviewAppointment;

        /**
         * 面试通知
         */
        private String interview;

        /**
         * 入职通知
         */
        private String onboarding;

        /**
         * 入职进度通知
         */
        private String onboardingProgress;
    }
}
