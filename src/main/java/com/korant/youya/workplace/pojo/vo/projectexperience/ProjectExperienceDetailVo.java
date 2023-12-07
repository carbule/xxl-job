package com.korant.youya.workplace.pojo.vo.projectexperience;

import lombok.Data;

/**
 * @ClassName ProjectExperienceDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 16:03
 * @Version 1.0
 */
@Data
public class ProjectExperienceDetailVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 工作履历id
     */
    private Long weId;

    /**
     * 企业名称
     */
    private String enterpriseName;

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
