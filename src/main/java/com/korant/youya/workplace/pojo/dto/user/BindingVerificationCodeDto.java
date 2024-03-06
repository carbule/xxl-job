package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName BindingVerificationCodeDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/5 16:10
 * @Version 1.0
 */
@Data
public class BindingVerificationCodeDto {

    @NotBlank(message = "支付宝账号不能为空")
    private String alipayAccount;
}
