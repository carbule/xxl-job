package com.korant.youya.workplace.pojo.dto.workexperience;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 15:50
 * @PackageName:com.korant.youya.workplace.pojo.dto.workexperience
 * @ClassName: WorkExperienceCreateDto
 * @Description: TODO
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
    @NotBlank(message = "开始时间不能为空")
    private String startTime;

    /**
     * 结束时间
     */
    @NotBlank(message = "结束时间不能为空")
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
