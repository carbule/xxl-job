package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName EnterpriseJoinDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/13 10:17
 * @Version 1.0
 */
@Data
public class EnterpriseJoinDto {

    /**
     * 企业id
     */
    @NotNull(message = "企业id不能为空")
    private Long enterpriseId;
}
