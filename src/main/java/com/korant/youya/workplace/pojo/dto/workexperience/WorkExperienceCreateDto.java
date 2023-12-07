package com.korant.youya.workplace.pojo.dto.workexperience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName WorkExperienceCreateDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 15:29
 * @Version 1.0
 */
@Data
public class WorkExperienceCreateDto {

    /**
     * 企业名称
     */
    @NotBlank(message = "企业名称不能为空")
    private String enterpriseName;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    private String departmentName;

    /**
     * 职位名称
     */
    @NotBlank(message = "职位名称不能为空")
    private String positionName;

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
     * 工作内容
     */
    @Size(max = 500, message = "最多不得超过500字")
    private String content;

    /**
     * 工作业绩
     */
    @Size(max = 500, message = "最多不得超过500字")
    private String performance;
}
