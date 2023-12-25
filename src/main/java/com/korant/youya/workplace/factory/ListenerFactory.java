package com.korant.youya.workplace.factory;

import com.rabbitmq.client.DefaultConsumer;

/**
 * @author chenyiqiang
 * @date 2022-11-08
 */
public interface ListenerFactory {

    /**
     * 根据类型创建监听
     *
     * @param type
     * @return
     */
    DefaultConsumer createListener(String type);
}
