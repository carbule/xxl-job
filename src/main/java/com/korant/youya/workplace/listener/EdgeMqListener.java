package com.korant.youya.workplace.listener;

import com.alibaba.fastjson2.JSONObject;
import com.korant.youya.workplace.pojo.dto.graph.SharedDto;
import com.korant.youya.workplace.service.GraphSharedService;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class EdgeMqListener {

    @Resource
    private GraphSharedService sharedService;

    @RabbitListener(queues = "job.share")
    public void jobShareQueue(Channel channel, Message msg) {
        log.info("job.share，收到消息");
        String messageBodyString = new String(msg.getBody());
        try {
            SharedDto sharedDto = JSONObject.parseObject(messageBodyString, SharedDto.class);
            sharedService.insertJobShared(sharedDto);
        } catch (Exception e) {
            log.error("job.share处理失败，原因：", e);
        } finally {
            try {
                channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RabbitListener(queues = "huntjob.share")
    public void huntJobShareQueue(Channel channel, Message msg) {
        log.info("huntjob.share，收到消息");
        String messageBodyString = new String(msg.getBody());
        try {
            SharedDto sharedDto = JSONObject.parseObject(messageBodyString, SharedDto.class);
            sharedService.insertHuntJobShared(sharedDto);
        } catch (Exception e) {
            log.error("huntjob.share处理失败，原因：", e);
        } finally {
            try {
                channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
