package com.korant.youya.workplace.pojo.vo.educationexperience;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 16:39
 * @PackageName:com.korant.youya.workplace.pojo.vo.educationexperience
 * @ClassName: EducationExperienceListVo
 * @Description: TODO
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
    private Integer eduLevel;

    /**
     * 开始时间
     */
    private LocalDate startTime;

    /**
     * 结束时间
     */
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
