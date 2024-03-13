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
        switch (type) {
            case "enterpriseQrcode":
                return new EnterpriseQrcodeListener(channel);
            case "enterpriseShareImage":
                return new EnterpriseShareImageListener(channel);
            case "huntJobShareImage":
                return new HuntJobShareImageListener(channel);
            case "jobShareImage":
                return new JobShareImageListener(channel);
            case "userOrderTimeout":
                return new UserOrderTimeoutListener(channel);
            case "enterpriseOrderTimeout":
                return new EnterpriseOrderTimeoutListener(channel);
            case "closeUserOrder":
                return new CloseUserOrderListener(channel);
            case "closeEnterpriseOrder":
                return new CloseEnterpriseOrderListener(channel);
            default:
                return null;
        }
    }
}
