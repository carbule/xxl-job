package com.korant.youya.workplace.pojo.vo.workexperience;

import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperiencePreviewListVo;
import lombok.Data;

import java.util.List;

/**
 * @Date 2023/11/17 15:07
 * @ClassName: WorkExperiencePreviewVo
 * @Description:
 * @Version 1.0
 */
@Data
public class WorkExperiencePreviewVo {

    /**
     * id
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
     * 工作内容
     */
    private String content;

    /**
     * 工作业绩
     */
    private String performance;

    /**
     * 项目经验列表
     */
    private List<ProjectExperiencePreviewListVo> experiencePreviewListVoList;

}
