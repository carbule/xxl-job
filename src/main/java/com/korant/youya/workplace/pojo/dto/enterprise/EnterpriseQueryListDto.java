package com.korant.youya.workplace.pojo.dto.enterprise;

import com.korant.youya.workplace.pojo.PageParam;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/20 16:54
 * @PackageName:com.korant.youya.workplace.pojo.dto.enterprise
 * @ClassName: EnterpriseQueryListDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class EnterpriseQueryListDto extends PageParam {

    /**
     * 企业名称
     */
    @NotBlank(message = "企业名称不能为空")
    private String name;

}
