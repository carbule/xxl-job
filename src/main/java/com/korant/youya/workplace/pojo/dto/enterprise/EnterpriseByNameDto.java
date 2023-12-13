package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName EnterpriseByNameDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/13 10:22
 * @Version 1.0
 */
@Data
public class EnterpriseByNameDto {

    /**
     * 企业名称
     */
    @NotBlank(message = "企业名称不能为空")
    private String enterpriseName;
}
