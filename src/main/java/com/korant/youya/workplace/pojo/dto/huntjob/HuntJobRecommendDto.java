package com.korant.youya.workplace.pojo.dto.huntjob;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName HuntJobRecommendDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/3 15:05
 * @Version 1.0
 */
@Data
public class HuntJobRecommendDto {

    /**
     * 求职id
     */
    @NotNull(message = "求职id不能为空")
    private Long huntJobId;

    /**
     * hr
     */
    @NotNull(message = "hr不能为空")
    private Long hr;
}
