package com.korant.youya.workplace.pojo.vo.educationexperience;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

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
    private String educationExperienceStartTime;

    /**
     * 结束时间
     */
    private String educationExperienceEndTime;

    /**
     * 专业名称
     */
    private String specialityName;

    /**
     * 学位名称
     */
    private String degreeName;
}
