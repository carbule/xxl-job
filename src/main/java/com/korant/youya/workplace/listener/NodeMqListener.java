package com.korant.youya.workplace.listener;

import com.korant.youya.workplace.pojo.dto.user.UserGraphDto;
import com.korant.youya.workplace.pojo.po.UserGraph;
import com.korant.youya.workplace.service.GraphUserService;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NodeMqListener {
    @Resource
    private GraphUserService userService;

    @RabbitListener(queues = "user")
    public void userQueue(UserGraph userGraph) {
        userService.insert(userGraph);
    }

    @RabbitListener(queues = "user.del")
    public void userDelQueue(@NotNull UserGraphDto userGraphDto) {
        userService.deleteById(userGraphDto.getId());
    }
}
