package com.korant.youya.workplace.utils;

import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @ClassName RabbitMqUtil
 * @Description rabbitmq工具类
 * @Author chenyiqiang
 * @Date 2023/7/18 16:11
 * @Version 1.0
 */
@Configuration
@Slf4j
public class RabbitMqUtil {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ConnectionFactory connectionFactory;

    /**
     * 发送消息到指定延迟队列
     *
     * @param exchange   交换机
     * @param routingKey 路由key
     * @param obj        数据
     */
    public void sendTTLMsg(String exchange, String routingKey, Object obj) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, obj);
        } catch (Exception e) {
            log.error("推送消息至队列失败,错误原因:{}", e.getMessage());
        }
    }

    /**
     * 指定过期时间推送消息至延迟队列
     *
     * @param message
     * @param delayedTime
     */
    public void sendDelayedMsg(String exchangeName, String routingKey, Object message, int delayedTime) {
        log.info("当前时间：{}, 发送一条时长为{}秒的消息给延迟队列delayed_queue：{}", new Date().toString(), delayedTime, message);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message, msg -> {
            //设置消息的延迟时长
            msg.getMessageProperties().setDelay(delayedTime * 1000);
            return msg;
        });
    }

    /**
     * 指定队列发送消息
     *
     * @param queueName
     * @param message
     * @throws IOException
     */
    public void basicPublish(String queueName, Object message) {
        Channel channel = null;
        try {
            Connection connection = connectionFactory.createConnection();
            channel = connection.createChannel(false);
            channel.basicPublish("", queueName, null, String.valueOf(message).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {

        } finally {
            assert channel != null;
            try {
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
