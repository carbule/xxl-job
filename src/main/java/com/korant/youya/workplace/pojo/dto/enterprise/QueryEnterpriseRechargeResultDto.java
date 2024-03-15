package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName QueryEnterpriseRechargeResultDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/8 10:37
 * @Version 1.0
 */
@Data
public class QueryEnterpriseRechargeResultDto {

    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空")
    private Long orderId;
}