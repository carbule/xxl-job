package com.korant.youya.workplace.pojo.vo.educationexperience;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

/**
 * @Date 2023/11/16 16:39
 * @ClassName: EducationExperienceListVo
 * @Description:
 * @Version 1.0
 */
@Data
public class EducationExperienceListVo {

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
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 专业名称
     */
    private String specialityName;

    /**
     * 学位名称
     */
    private String degreeName;

}
