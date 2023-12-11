package com.korant.youya.workplace.pojo.vo.projectexperience;

import lombok.Data;

/**
 * @ClassName ProjectExperienceVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 16:03
 * @Version 1.0
 */
@Data
public class ProjectExperienceVo {

    /**
     * 主键
     */
    private Long peId;

    /**
     * 企业名称
     */
    private String projectEnterpriseName;

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
