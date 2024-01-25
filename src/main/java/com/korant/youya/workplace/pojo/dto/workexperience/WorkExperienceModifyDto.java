package com.korant.youya.workplace.pojo.dto.workexperience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @ClassName WorkExperienceModifyDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 15:29
 * @Version 1.0
 */
@Data
public class WorkExperienceModifyDto {

    /**
     * 主键
     */
    @NotNull(message = "工作履历id不能为空")
    private Long id;

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
    @NotBlank(message = "开始时间不能为空")
    @Pattern(regexp = "^(\\d{4})-(\\d{2})$", message = "年月参数格式错误，应为 'yyyy-MM' 格式")
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 工作履历是否仍在进行中 0-否 1-是
     */
    @NotNull(message = "工作履历是否仍在进行中状态不能为空")
    private Integer isCurrent;

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
