package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName EnterpriseCompletePaymentDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/8 10:36
 * @Version 1.0
 */
@Data
public class EnterpriseCompletePaymentDto {

    /**
     * 订单id
     */
    @NotBlank(message = "订单id不能为空")
    private String orderId;
}
