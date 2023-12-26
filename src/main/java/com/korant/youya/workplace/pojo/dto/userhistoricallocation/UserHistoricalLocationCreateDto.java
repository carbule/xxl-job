package com.korant.youya.workplace.pojo.dto.userhistoricallocation;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName UserHistoricalLocationCreateDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/26 15:28
 * @Version 1.0
 */
@Data
public class UserHistoricalLocationCreateDto {

    /**
     * 城市编码
     */
    @NotBlank(message = "城市编码不能为空")
    private String cityCode;
}
