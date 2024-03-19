package com.korant.youya.workplace.factory;

import com.korant.youya.workplace.listener.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenyiqiang
 * @date 2022-11-08
 */
@Data
public class RabbitMqListenerFactory implements ListenerFactory {

    private Channel channel;

    public RabbitMqListenerFactory(Channel channel) {
        this.channel = channel;
    }

    /**
     * 根据类型创建rabbitmq消费者
     *
     * @param type
     * @return
     */
    @Override
    public DefaultConsumer createListener(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        return switch (type) {
            case "enterpriseQrcode" -> new EnterpriseQrcodeListener(channel);
            case "enterpriseShareImage" -> new EnterpriseShareImageListener(channel);
            case "huntJobShareImage" -> new HuntJobShareImageListener(channel);
            case "jobShareImage" -> new JobShareImageListener(channel);
            case "userOrderPaymentInquiry" -> new UserOrderPaymentInquiryListener(channel);
            case "enterpriseOrderPaymentInquiry" -> new EnterpriseOrderPaymentInquiryListener(channel);
            case "userOrderTimeout" -> new UserOrderTimeoutListener(channel);
            case "enterpriseOrderTimeout" -> new EnterpriseOrderTimeoutListener(channel);
            case "closeUserOrder" -> new CloseUserOrderListener(channel);
            case "closeEnterpriseOrder" -> new CloseEnterpriseOrderListener(channel);
            default -> null;
        };
    }
}
