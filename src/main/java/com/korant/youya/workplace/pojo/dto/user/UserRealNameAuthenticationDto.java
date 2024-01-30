package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName UserRealNameAuthenticationDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/7/26 16:59
 * @Version 1.0
 */
@Data
public class UserRealNameAuthenticationDto {

    /**
     * 用户姓氏
     */
    @NotBlank(message = "姓氏不能为空")
    private String lastName;

    /**
     * 用户名
     */
    @NotBlank(message = "名字不能为空")
    private String firstName;
}
