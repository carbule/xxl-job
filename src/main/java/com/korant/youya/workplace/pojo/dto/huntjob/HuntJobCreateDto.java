package com.korant.youya.workplace.pojo.dto.huntjob;

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
     * 推荐奖励
     */
    private Integer award;

    /**
     * 面试奖励分配比例
     */
    private Integer interviewRewardRate;

    /**
     * 入职奖励分配比例
     */
    private Integer onboardRewardRate;

    /**
     * 转正奖励分配比例
     */
    private Integer fullMemberRewardRate;
}
