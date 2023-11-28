package com.korant.youya.workplace.pojo.dto.workexperience;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/16 15:51
 * @ClassName: WorkExperienceModifyDto
 * @Description:
 * @Version 1.0
 */
@Data
public class WorkExperienceModifyDto {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 企业名称
     */
    @NotNull(message = "企业名称不能为空")
    private String enterpriseName;

    /**
     * 部门名称
     */
    @NotNull(message = "部门名称不能为空")
    private String departmentName;

    /**
     * 职位名称
     */
    @NotNull(message = "职位名称不能为空")
    private String positionName;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private String startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private String endTime;

    /**
     * 工作内容
     */
    private String content;

    /**
     * 工作业绩
     */
    private String performance;

}
