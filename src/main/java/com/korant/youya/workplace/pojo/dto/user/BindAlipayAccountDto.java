package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName BindAlipayAccountDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/5 16:35
 * @Version 1.0
 */
@Data
public class BindAlipayAccountDto {

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

    /**
     * 支付宝账号
     */
    @NotBlank(message = "支付宝账号不能为空")
    private String alipayAccount;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证不能为空")
    private String idcard;
}
