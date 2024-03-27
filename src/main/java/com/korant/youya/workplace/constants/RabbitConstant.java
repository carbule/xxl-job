package com.korant.youya.workplace.constants;

/**
 * 消息队列常量类
 *
 * @author zhouzhifeng
 * @since 2024/3/26 18:00
 */
public interface RabbitConstant {

    /**
     * 交换机常量
     */
    interface Exchange {

        /**
         * 面试预约通知交换机
         */
        String WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_EXCHANGE = "wx.message-subscribe.interview-appointment.exchange";
    }

    /**
     * 队列常量
     */
    interface Queue {

        /**
         * 面试预约通知队列-候选人
         */
        String WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_4CANDIDATE_QUEUE = "wx.message-subscribe.interview-appointment.candidate.queue";

        /**
         * 面试预约通知队列-人才库
         */
        String WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_4TALENT_POOL_QUEUE = "wx.message-subscribe.interview-appointment.talent-pool.queue";
    }

    /**
     * 路由常量
     */
    interface RoutingKey {

        /**
         * 面试预约通知队列-候选人
         */
        String WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_4CANDIDATE_ROUTING_KEY = "wx.message-subscribe.interview-appointment.candidate.routing-key";

        /**
         * 面试预约通知队列-人才库
         */
        String WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_4TALENT_POOL_ROUTING_KEY = "wx.message-subscribe.interview-appointment.talent-pool.routing-key";
    }
}
