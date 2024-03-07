package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName UserRechargeDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/2/29 14:18
 * @Version 1.0
 */
@Data
public class UserRechargeDto {

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
