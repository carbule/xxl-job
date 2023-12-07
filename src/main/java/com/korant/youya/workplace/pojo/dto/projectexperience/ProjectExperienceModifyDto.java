package com.korant.youya.workplace.pojo.dto.projectexperience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @ClassName ProjectExperienceModifyDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 16:03
 * @Version 1.0
 */
@Data
public class ProjectExperienceModifyDto {

    /**
     * 主键
     */
    @NotNull(message = "项目经验id不能为空")
    private Long id;

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
    @Size(max = 500, message = "最多不得超过500字")
    private String projectContent;
}
