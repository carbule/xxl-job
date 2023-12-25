package com.korant.youya.workplace.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenyiqiang
 * @date 2022-04-20
 */
@Configuration
@Slf4j
public class RabbitMqConfig {

    /**
     * Rabbitmq 生产者
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        //确认消息送到交换机(Exchange)回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("----------- 确认消息送到交换机(Exchange)结果 -----------");
                log.info("发送结果 => " + (ack ? "成功" : "失败"));
            }
        });

        //确认消息送到队列(Queue)回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(@NotNull ReturnedMessage returnedMessage) {
                log.info("----------- 确认消息送到队列(Queue)结果 -----------");
                log.info("发生消息：{}", returnedMessage.getMessage());
                log.info("交换机：{}", returnedMessage.getExchange());
                log.info("路由键：{}", returnedMessage.getRoutingKey());
            }
        });
        return rabbitTemplate;
    }

    /**
     * Rabbitmq 消费者
     */
    @Bean(name = "mqListenerFactory")
    public SimpleRabbitListenerContainerFactory mqListenerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        configurer.configure(listenerContainerFactory, connectionFactory);
        return listenerContainerFactory;
    }
}
