package com.korant.youya.workplace.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;

//@Configuration
public class RabbitMqMsgConvert {

    @Bean
    public Jackson2JsonMessageConverter jacksonMessageConverter() {
        var j2jmc = new Jackson2JsonMessageConverter();
        j2jmc.setCreateMessageIds(true);
        return j2jmc;
    }
}
