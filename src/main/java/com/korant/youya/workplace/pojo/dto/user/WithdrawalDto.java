package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName WithdrawalDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/3/5 19:38
 * @Version 1.0
 */
@Data
public class WithdrawalDto {

    @NotBlank(message = "提现金额不能为空")
    private String amount;
}
