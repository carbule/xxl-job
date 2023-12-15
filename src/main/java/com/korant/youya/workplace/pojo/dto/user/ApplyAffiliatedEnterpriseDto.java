package com.korant.youya.workplace.pojo.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName ApplyAffiliatedEnterpriseDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/15 11:24
 * @Version 1.0
 */
@Data
public class ApplyAffiliatedEnterpriseDto {

    /**
     * 企业id
     */
    @NotNull(message = "企业id不能为空")
    private Long enterpriseId;
}
