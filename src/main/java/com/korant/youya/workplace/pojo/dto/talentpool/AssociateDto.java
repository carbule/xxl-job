package com.korant.youya.workplace.pojo.dto.talentpool;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName AssociateDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/8 17:14
 * @Version 1.0
 */
@Data
public class AssociateDto {

    /**
     * 人才id
     */
    @NotNull(message = "人才id不能为空")
    private Long id;

    /**
     * 职位
     */
    @NotNull(message = "职位id不能为空")
    private Long jobId;
}
