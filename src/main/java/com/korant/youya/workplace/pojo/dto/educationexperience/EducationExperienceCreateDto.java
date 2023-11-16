package com.korant.youya.workplace.pojo.dto.educationexperience;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 16:43
 * @PackageName:com.korant.youya.workplace.pojo.dto.educationexperience
 * @ClassName: EducationExperienceCreateDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class EducationExperienceCreateDto {

    /**
     * 学校名称
     */
    @NotBlank(message =  "学校名称不能为空")
    private String schoolName;

    /**
     * 学历
     */
    @NotBlank(message =  "学历不能为空")
    private Integer eduLevel;

    /**
     * 开始时间
     */
    @NotBlank(message =  "开始时间不能为空")
    private LocalDate startTime;

    /**
     * 结束时间
     */
    @NotBlank(message =  "结束时间不能为空")
    private LocalDate endTime;

    /**
     * 专业名称
     */
    @NotBlank(message =  "专业名称不能为空")
    private String specialityName;

    /**
     * 学位名称
     */
    private String degreeName;

}
