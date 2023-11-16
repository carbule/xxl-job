package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName UserLoginByWechatCodeDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/14 16:43
 * @Version 1.0
 */
@Data
public class UserLoginByWechatCodeDto {

    /**
     * 前端传入的微信code
     */
    @NotBlank(message = "code不能为空")
    private String code;
}
