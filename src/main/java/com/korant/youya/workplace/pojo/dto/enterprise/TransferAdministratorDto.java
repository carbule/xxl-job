package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName TransferAdministratorDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/14 14:12
 * @Version 1.0
 */
@Data
public class TransferAdministratorDto {

    /**
     * 用户id
     */
    @NotNull(message = "转让人id不能为空")
    private Long userId;
}
