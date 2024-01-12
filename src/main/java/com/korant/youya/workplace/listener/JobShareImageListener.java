package com.korant.youya.workplace.listener;

import com.korant.youya.workplace.service.JobQrCodeService;
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
public class JobShareImageListener extends DefaultConsumer {

    public JobShareImageListener(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
        try {
            log.info("[JobShareImageListener]接收到消息");
            String msg = new String(body);
            log.info("消息内容:{}", msg);
            JobQrCodeService jobQrCodeService = SpringContextUtils.getBean(JobQrCodeService.class);
            jobQrCodeService.deleteShareImage(msg);
        } catch (Exception e) {
            log.error("[JobShareImageListener]接收消息异常:", e);
        } finally {
            try {
                getChannel().basicAck(envelope.getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
