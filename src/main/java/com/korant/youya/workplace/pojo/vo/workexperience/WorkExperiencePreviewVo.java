package com.korant.youya.workplace.pojo.vo.workexperience;

import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperiencePreviewVo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName WorkExperiencePreviewVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/27 15:58
 * @Version 1.0
 */
@Data
public class WorkExperiencePreviewVo {

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
    private List<ProjectExperiencePreviewVo> projectExperienceVoList;
}
