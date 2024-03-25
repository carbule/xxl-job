package com.korant.youya.workplace.pojo.dto.huntjob;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName HuntJobCreateDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/21 17:32
 * @Version 1.0
 */
@Data
public class HuntJobCreateDto {

    /**
     * 意向职位id
     */
    @NotNull(message = "意向职位不能为空")
    private Long positionId;

    /**
     * 期望区域id
     */
    @NotNull(message = "期望区域不能为空")
    private Long areaId;

    /**
     * 入职成功奖金
     */
    private String onboardingAward;

    /**
     * 求职说明
     */
    @NotBlank(message = "求职说明不能为空")
    private String description;
}
