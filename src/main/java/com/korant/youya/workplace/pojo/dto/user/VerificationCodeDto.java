package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName VerificationCodeDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/10/9 17:36
 * @Version 1.0
 */
@Data
public class VerificationCodeDto {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;
}
