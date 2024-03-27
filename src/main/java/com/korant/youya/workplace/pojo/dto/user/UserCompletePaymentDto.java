package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName UserCompletePaymentDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/1 14:00
 * @Version 1.0
 */
@Data
public class UserCompletePaymentDto {

    /**
     * 订单id
     */
    @NotBlank(message = "订单id不能为空")
    private String orderId;
}
