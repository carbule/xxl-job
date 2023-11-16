package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName UserLoginBySMSVerificationCodeDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/14 16:47
 * @Version 1.0
 */
@Data
public class UserLoginBySMSVerificationCodeDto {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
}
