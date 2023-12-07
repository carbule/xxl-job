package com.korant.youya.workplace.pojo.vo.educationexperience;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName EducationExperienceDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 16:52
 * @Version 1.0
 */
@Data
public class EducationExperienceDetailVo {

    /**
     * 主键
     */
    private Long id;

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
    private LocalDate startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM")
    private LocalDate endTime;

    /**
     * 专业名称
     */
    private String specialityName;

    /**
     * 学位名称
     */
    private String degreeName;
}
