package com.korant.youya.workplace.listener;

import com.korant.youya.workplace.service.UserService;
import com.korant.youya.workplace.utils.SpringContextUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @ClassName EnterpriseQrcodeListener
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/22 16:07
 * @Version 1.0
 */
@Slf4j
public class UserOrderTimeoutListener extends DefaultConsumer {

    public UserOrderTimeoutListener(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        log.info("[UserOrderTimeoutListener]接收到消息");
        String msg = new String(body);
        log.info("消息内容:{}", msg);
        UserService userService = SpringContextUtils.getBean(UserService.class);
        userService.orderTimeoutProcessing(Long.valueOf(msg));
        getChannel().basicAck(envelope.getDeliveryTag(), false);
    }
}
