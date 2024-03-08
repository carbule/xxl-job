package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName UserQueryRechargeResultDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/1 14:34
 * @Version 1.0
 */
@Data
public class UserQueryRechargeResultDto {

    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空")
    private Long orderId;
}
