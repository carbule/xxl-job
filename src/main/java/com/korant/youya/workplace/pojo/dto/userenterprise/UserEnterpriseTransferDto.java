package com.korant.youya.workplace.pojo.dto.userenterprise;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/24 15:21
 * @PackageName:com.korant.youya.workplace.pojo.vo.userenterprise
 * @ClassName: UserEnterpriseTransferDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class UserEnterpriseTransferDto {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    private Long uid;

}
