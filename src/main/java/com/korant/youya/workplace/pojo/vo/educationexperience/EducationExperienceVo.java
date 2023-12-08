package com.korant.youya.workplace.pojo.vo.educationexperience;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName EducationExperienceVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 16:52
 * @Version 1.0
 */
@Data
public class EducationExperienceVo {

    /**
     * 主键
     */
    private Long eeId;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 学历
     */
    @Dict(categoryCode = "education")
    private Integer eduLevel;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM")
    private LocalDate educationExperienceStartTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM")
    private LocalDate educationExperienceEndTime;

    /**
     * 专业名称
     */
    private String specialityName;

    /**
     * 学位名称
     */
    private String degreeName;
}
