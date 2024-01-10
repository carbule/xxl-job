package com.korant.youya.workplace.pojo.dto.talentpool;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName TalentPoolCreateConfirmationDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/10 16:01
 * @Version 1.0
 */
@Data
public class TalentPoolCreateConfirmationDto {

    /**
     * 主键
     */
    @NotNull(message = "人才id不能为空")
    private Long id;

    /**
     * 转正时间
     */
    @NotNull(message = "转正时间不能为空")
    private LocalDateTime confirmationTime;

    /**
     * 转正薪资
     */
    @NotNull(message = "转正薪资不能为空")
    private Integer salary;

    /**
     * 备注
     */
    private String note;
}
