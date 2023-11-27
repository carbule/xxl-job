package com.korant.youya.workplace.pojo.vo.huntjob;

import lombok.Data;

import java.util.List;

/**
 * @ClassName WorkExperienceVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/27 15:58
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
     * 工作内容
     */
    private String content;

    /**
     * 工作业绩
     */
    private String performance;

    /**
     * 项目经验
     */
    private List<ProjectExperienceVo> projectExperienceVoList;
}
