package com.korant.youya.workplace.pojo.dto.userblockedenterprise;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName UserBlockedEnterpriseCreateDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/23 19:08
 * @Version 1.0
 */
@Data
public class UserBlockedEnterpriseCreateDto {

    /**
     * 企业id
     */
    @NotNull(message = "企业id不能为空")
    private Long enterpriseId;
}
