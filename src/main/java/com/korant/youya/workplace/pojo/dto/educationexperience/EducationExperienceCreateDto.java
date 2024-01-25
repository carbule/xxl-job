package com.korant.youya.workplace.pojo.dto.educationexperience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @ClassName EducationExperienceCreateDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 16:51
 * @Version 1.0
 */
@Data
public class EducationExperienceCreateDto {

    /**
     * 学校名称
     */
    @NotBlank(message = "学校名称不能为空")
    private String schoolName;

    /**
     * 学历
     */
    @NotNull(message = "学历不能为空")
    private Integer eduLevel;

    /**
     * 开始时间
     */
    @NotBlank(message = "开始时间不能为空")
    @Pattern(regexp = "^(\\d{4})-(\\d{2})$", message = "年月参数格式错误，应为 'yyyy-MM' 格式")
    private String startTime;

    /**
     * 结束时间
     */
    @NotBlank(message = "结束时间不能为空")
    @Pattern(regexp = "^(\\d{4})-(\\d{2})$", message = "年月参数格式错误，应为 'yyyy-MM' 格式")
    private String endTime;

    /**
     * 专业名称
     */
    @NotBlank(message = "专业名称不能为空")
    private String specialityName;

    /**
     * 学位名称
     */
    private String degreeName;
}
