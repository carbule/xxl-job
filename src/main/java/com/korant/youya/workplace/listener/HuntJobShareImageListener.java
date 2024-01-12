package com.korant.youya.workplace.listener;

import com.korant.youya.workplace.service.HuntJobQrCodeService;
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
public class HuntJobShareImageListener extends DefaultConsumer {

    public HuntJobShareImageListener(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
        try {
            log.info("[HuntJobShareImageListener]接收到消息");
            String msg = new String(body);
            log.info("消息内容:{}", msg);
            HuntJobQrCodeService huntJobQrCodeService = SpringContextUtils.getBean(HuntJobQrCodeService.class);
            huntJobQrCodeService.deleteShareImage(msg);
        } catch (Exception e) {
            log.error("[HuntJobShareImageListener]接收消息异常:", e);
        } finally {
            try {
                getChannel().basicAck(envelope.getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
