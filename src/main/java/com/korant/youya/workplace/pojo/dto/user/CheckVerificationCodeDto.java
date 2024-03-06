package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName CheckVerificationCodeDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/5 17:35
 * @Version 1.0
 */
@Data
public class CheckVerificationCodeDto {

    /**
     * 支付宝账号
     */
    @NotBlank(message = "支付宝账号不能为空")
    private String alipayAccount;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
}
