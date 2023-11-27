package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Date 2023/11/20 16:54
 * @PackageName:com.korant.youya.workplace.pojo.dto.enterprise
 * @ClassName: EnterpriseQueryListDto
 * @Description:
 * @Version 1.0
 */
@Data
public class EnterpriseQueryListDto{

    /**
     * 企业名称
     */
    @NotBlank(message = "企业名称不能为空")
    private String name;

}
