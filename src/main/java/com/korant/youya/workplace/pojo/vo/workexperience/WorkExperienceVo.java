package com.korant.youya.workplace.pojo.vo.workexperience;

import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName WorkExperienceVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/8 16:25
 * @Version 1.0
 */
@Data
public class WorkExperienceVo {

    /**
     * 主键
     */
    private Long weId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 职位名称
     */
    private String expectedPositionName;

    /**
     * 开始时间
     */
    private LocalDate workExperienceStartTime;

    /**
     * 结束时间
     */
    private LocalDate workExperienceEndTime;

    /**
     * 工作内容
     */
    private String content;

    /**
     * 工作业绩
     */
    private String performance;
}
