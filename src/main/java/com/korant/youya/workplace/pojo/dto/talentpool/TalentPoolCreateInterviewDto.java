package com.korant.youya.workplace.pojo.dto.talentpool;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName TalentPoolCreateInterviewDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/10 16:00
 * @Version 1.0
 */
@Data
public class TalentPoolCreateInterviewDto {

    /**
     * 主键
     */
    @NotNull(message = "人才id不能为空")
    private Long id;

    /**
     * 面试方式
     */
    @NotNull(message = "面试方式不能为空")
    private Integer approach;

    /**
     * 面试时间
     */
    @NotNull(message = "面试时间不能为空")
    private LocalDateTime interTime;

    /**
     * 备注
     */
    private String note;
}
