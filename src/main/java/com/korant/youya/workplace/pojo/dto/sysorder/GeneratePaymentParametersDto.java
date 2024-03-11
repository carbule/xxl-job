package com.korant.youya.workplace.pojo.dto.sysorder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName GeneratePaymentParametersDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/11 14:30
 * @Version 1.0
 */
@Data
public class GeneratePaymentParametersDto {

    /**
     * 前端传入的微信code
     */
    @NotBlank(message = "code不能为空")
    private String code;

    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空")
    private Long orderId;
}
