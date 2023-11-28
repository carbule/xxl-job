package com.korant.youya.workplace.pojo.dto.projectexperience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/16 16:12
 * @ClassName: ProjectExperienceCreateDto
 * @Description:
 * @Version 1.0
 */
@Data
public class ProjectExperienceCreateDto {

    /**
     * 工作履历id
     */
    @NotNull(message = "工作履历id不能为空")
    private Long weId;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    /**
     * 项目角色
     */
    @NotBlank(message = "项目角色不能为空")
    private String projectRole;

    /**
     * 项目内容
     */
    private String projectContent;

}
