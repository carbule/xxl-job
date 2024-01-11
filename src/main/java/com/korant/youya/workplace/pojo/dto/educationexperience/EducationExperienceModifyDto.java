package com.korant.youya.workplace.pojo.dto.educationexperience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName EducationExperienceModifyDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 16:51
 * @Version 1.0
 */
@Data
public class EducationExperienceModifyDto {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    private Long id;

    /**
     * 学校名称
     */
    @NotBlank(message = "学校名称不能为空")
    private String schoolName;

    /**
     * 学历
     */
    @NotNull(message = "学历不能为空")
    private Integer eduLevel;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDate startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private LocalDate endTime;

    /**
     * 专业名称
     */
    @NotBlank(message = "专业名称不能为空")
    private String specialityName;

    /**
     * 学位名称
     */
    private String degreeName;
}