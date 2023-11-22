package com.korant.youya.workplace.pojo.vo.educationexperience;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 16:45
 * @PackageName:com.korant.youya.workplace.pojo.vo.educationexperience
 * @ClassName: EducationExperienceDetailVo
 * @Description: TODO
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
