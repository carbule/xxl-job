package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName EnterpriseModifyLogoDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/12 10:08
 * @Version 1.0
 */
@Data
public class EnterpriseModifyLogoDto {

    /**
     * 企业id
     */
    @NotNull(message = "企业id不能为空")
    private Long id;

    /**
     * 企业logo
     */
    private String logo;
}
