package com.korant.youya.workplace.pojo.dto.candidate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName CandidateCreateConfirmationDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/9 15:47
 * @Version 1.0
 */
@Data
public class CandidateCreateConfirmationDto {

    /**
     * 主键
     */
    @NotNull(message = "候选id不能为空")
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
