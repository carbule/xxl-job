package com.korant.youya.workplace.pojo.dto.userenterprise;

import com.korant.youya.workplace.pojo.PageParam;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/24 9:49
 * @ClassName: UserEnterpriseQueryVo
 * @Version 1.0
 */
@Data
public class UserEnterpriseQueryListDto extends PageParam {

    /**
     * 公司id
     */
    @NotNull(message = "公司id不能为空")
    private Long id;

    /**
     * 姓名
     */
    private String name;

}
