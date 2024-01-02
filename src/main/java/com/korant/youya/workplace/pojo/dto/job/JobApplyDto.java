package com.korant.youya.workplace.pojo.dto.job;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName JobApplyDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/2 14:43
 * @Version 1.0
 */
@Data
public class JobApplyDto {

    /**
     * 职位id
     */
    @NotNull(message = "职位id不能为空")
    private Long jobId;

    /**
     * 推荐人id
     */
    private Long referee;
}
