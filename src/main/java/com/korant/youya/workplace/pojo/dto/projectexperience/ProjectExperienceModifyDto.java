package com.korant.youya.workplace.pojo.dto.projectexperience;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/16 16:13
 * @ClassName: ProjectExperienceModifyDto
 * @Description:
 * @Version 1.0
 */
@Data
public class ProjectExperienceModifyDto {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空")
    private Long id;

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
