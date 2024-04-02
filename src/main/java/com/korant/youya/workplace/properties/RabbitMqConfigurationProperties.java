package com.korant.youya.workplace.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author chenyiqiang
 * @date 2022-07-10
 */
@Data
@Component
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqConfigurationProperties {

    private HashMap<String, BindingProperties> normalProperties;

    private HashMap<String, BindingProperties> delayProperties;
}
