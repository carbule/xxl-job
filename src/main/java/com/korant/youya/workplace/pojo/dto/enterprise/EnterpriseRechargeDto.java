package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName EnterpriseRechargeDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/8 10:36
 * @Version 1.0
 */
@Data
public class EnterpriseRechargeDto {

    /**
     * 前端传入的微信code
     */
    @NotBlank(message = "code不能为空")
    private String code;

    /**
     * 商品购买数量
     */
    @NotNull(message = "购买数量不能为空")
    private Integer quantity;
}
