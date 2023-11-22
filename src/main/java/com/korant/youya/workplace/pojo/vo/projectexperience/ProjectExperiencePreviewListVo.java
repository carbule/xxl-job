package com.korant.youya.workplace.pojo.vo.projectexperience;

import lombok.Data;

/**
 * @Date 2023/11/21 15:29
 * @PackageName:com.korant.youya.workplace.pojo.vo.workexperience
 * @ClassName: WorkExperiencePreviewListVo
 * @Description:
 * @Version 1.0
 */
@Data
public class ProjectExperiencePreviewListVo {

    /**
     * 项目id
     */
    private String projectId;

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
