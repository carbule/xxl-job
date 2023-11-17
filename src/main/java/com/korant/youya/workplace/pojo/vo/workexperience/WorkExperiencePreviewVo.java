package com.korant.youya.workplace.pojo.vo.workexperience;

import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/17 15:07
 * @PackageName:com.korant.youya.workplace.pojo.vo.workexperience
 * @ClassName: WorkExperiencePreviewVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class WorkExperiencePreviewVo {

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
     * 项目名称
     */
    private String projectName;

    /**
     * 项目角色
     */
    private String projectRole;

    /**
     * 项目内容
     */
    private String projectContent;

}
