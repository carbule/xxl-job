package com.korant.youya.workplace.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author chenyiqiang
 * @date 2022-07-10
 */
@Data
@Component
public class DelayProperties {

    /**
     * 交换机名称
     */
    private String exchangeName;

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * 路由键
     */
    private String routingKey;

    /**
     * 消费者类型
     */
    private String consumerType;
}
