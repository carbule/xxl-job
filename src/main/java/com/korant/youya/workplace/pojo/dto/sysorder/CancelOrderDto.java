package com.korant.youya.workplace.pojo.dto.sysorder;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName CancelOrderDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/11 17:29
 * @Version 1.0
 */
@Data
public class CancelOrderDto {

    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空")
    private Long orderId;
}
