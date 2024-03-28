package com.korant.youya.workplace.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqDataSyncConfig {

    // ------------- 实体数据同步 ---------------
    @Bean
    public DirectExchange entityDirectExchange() {  // 实体交换机
        return ExchangeBuilder.directExchange("youya.entity").build();
    }
    // 用户
    @Bean
    public Queue userQueue() {  // 队列
        return QueueBuilder.durable("user").build();
    }
    @Bean
    public Binding userBinding() {  // 绑定
        return BindingBuilder.bind(userQueue()).to(entityDirectExchange()).withQueueName();
    }
    @Bean
    public Queue userDelQueue() {  // 队列
        return QueueBuilder.durable("user.del").build();
    }
    @Bean
    public Binding userDelBinding() {  // 绑定
        return BindingBuilder.bind(userDelQueue()).to(entityDirectExchange()).withQueueName();
    }
    @Bean
    public Queue userRcvQueue() {  // 队列
        return QueueBuilder.durable("user.rcv").build();
    }
    @Bean
    public Binding userRcvBinding() {  // 绑定
        return BindingBuilder.bind(userRcvQueue()).to(entityDirectExchange()).withQueueName();
    }
    // 职位
    @Bean
    public Queue jobQueue() {  // 队列
        return QueueBuilder.durable("job").build();
    }
    @Bean
    public Binding jobBinding() {  // 绑定
        return BindingBuilder.bind(jobQueue()).to(entityDirectExchange()).withQueueName();
    }
    @Bean
    public Queue jobDelQueue() {  // 绑定
        return QueueBuilder.durable("job.del").build();
    }
    @Bean
    public Binding jobDelBinding() {  // 绑定
        return BindingBuilder.bind(jobDelQueue()).to(entityDirectExchange()).withQueueName();
    }
    @Bean
    public Queue jobRcvQueue() {  // 队列
        return QueueBuilder.durable("job.rcv").build();
    }
    @Bean
    public Binding jobRcvBinding() {  // 绑定
        return BindingBuilder.bind(jobRcvQueue()).to(entityDirectExchange()).withQueueName();
    }

    // ------------- 分享链数据同步 ---------------
    @Bean
    public DirectExchange shareDirectExchange() {  // 分享交换机
        return ExchangeBuilder.directExchange("youya.share").build();
    }
    // 分享求职
    @Bean
    public Queue shareHuntJobQueue() {  // 队列
        return QueueBuilder.durable("huntjob.share").build();
    }
    @Bean
    public Binding shareUserBinding() {  // 绑定
        return BindingBuilder.bind(shareHuntJobQueue()).to(shareDirectExchange()).withQueueName();
    }
    // 分享职位
    @Bean
    public Queue shareJobQueue() {  // 队列
        return QueueBuilder.durable("job.share").build();
    }
    @Bean
    public Binding shareJobBinding() {  // 绑定
        return BindingBuilder.bind(shareJobQueue()).to(shareDirectExchange()).withQueueName();
    }
}
