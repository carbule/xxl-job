package com.korant.youya.workplace.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName LettuceConnectionValidConfig
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/17 14:11
 * @Version 1.0
 */
@Component
public class LettuceConnectionValidConfig implements InitializingBean {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (redisConnectionFactory instanceof LettuceConnectionFactory) {
            LettuceConnectionFactory c = (LettuceConnectionFactory) redisConnectionFactory;
            c.setValidateConnection(true);
        }
    }
}
