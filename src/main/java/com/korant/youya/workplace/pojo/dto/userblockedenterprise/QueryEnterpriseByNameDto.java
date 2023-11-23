package com.korant.youya.workplace.pojo.dto.userblockedenterprise;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName QueryEnterpriseByNameDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/23 20:29
 * @Version 1.0
 */
@Data
public class QueryEnterpriseByNameDto {

    /**
     * 企业名称
     */
    @NotBlank(message = "企业名称不能为空")
    private String enterpriseName;
}
