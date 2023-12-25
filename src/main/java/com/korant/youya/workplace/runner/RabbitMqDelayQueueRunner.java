package com.korant.youya.workplace.runner;

import com.korant.youya.workplace.factory.RabbitMqListenerFactory;
import com.korant.youya.workplace.properties.DelayProperties;
import com.korant.youya.workplace.properties.RabbitMqConfigurationProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName RabbitMqDelayQueueRunner
 * @Description
 * @Author chenyiqiang
 * @Date 2023/9/26 14:32
 * @Version 1.0
 */
@Component
@Slf4j
public class RabbitMqDelayQueueRunner implements ApplicationRunner {

    @Resource
    RabbitMqConfigurationProperties mqConfigurationProperties;

    @Resource
    private ConnectionFactory connectionFactory;

    @Override
    public void run(ApplicationArguments args) {
        Channel channel = null;
        try {
            Connection connection = connectionFactory.createConnection();
            channel = connection.createChannel(false);
            Collection<DelayProperties> delayProperties = mqConfigurationProperties.getDelayProperties().values();
            if (!delayProperties.isEmpty()) {
                log.info("读取到延迟队列配置");
                for (DelayProperties property : delayProperties) {
                    String exchangeName = property.getExchangeName();
                    String queueName = property.getQueueName();
                    String routingKey = property.getRoutingKey();
                    String consumerType = property.getConsumerType();
                    Map<String, Object> map = new HashMap<>();
                    map.put("x-delayed-type", "direct");
                    //声明交换机
                    channel.exchangeDeclare(exchangeName, "x-delayed-message", true, false, map);
                    //声明队列
                    channel.queueDeclare(queueName, true, false, false, null);
                    //交换机队列绑定
                    channel.queueBind(queueName, exchangeName, routingKey);
//                    channel.basicQos(1);
                    RabbitMqListenerFactory rabbitMqListenerFactory = new RabbitMqListenerFactory(channel);
                    DefaultConsumer consumer = rabbitMqListenerFactory.createListener(consumerType);
                    channel.basicConsume(queueName, false, consumer);
                }
            }
            log.info("初始化延迟队列成功");
        } catch (Exception e) {
            log.error("初始化延迟队列发生错误,错误信息:", e);
        } finally {
            assert channel != null;
            try {
                channel.close();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
