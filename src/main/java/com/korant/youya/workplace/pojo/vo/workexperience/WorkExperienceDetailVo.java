package com.korant.youya.workplace.pojo.vo.workexperience;

import lombok.Data;

/**
 * @ClassName WorkExperienceDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 15:29
 * @Version 1.0
 */
@Data
public class WorkExperienceDetailVo {

    /**
     * 主键
     */
    private Long id;

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
    private String positionName;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 工作履历是否仍在进行中 0-否 1-是
     */
    private Integer isCurrent;

    /**
     * 工作内容
     */
    private String content;

    /**
     * 工作业绩
     */
    private String performance;
}
