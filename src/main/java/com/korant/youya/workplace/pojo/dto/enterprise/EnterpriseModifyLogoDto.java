package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/20 13:52
 * @PackageName:com.korant.youya.workplace.pojo.dto.enterprise
 * @ClassName: EnterpriseModifyLogoDto
 * @Description:
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
