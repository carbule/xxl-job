package com.korant.youya.workplace.pojo.vo.job;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

/**
 * @ClassName JobPersonalVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/4 11:54
 * @Version 1.0
 */
@Data
public class JobPersonalVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 学历要求
     */
    @Dict(categoryCode = "education")
    private Integer eduRequirements;

    /**
     * 工作经验
     */
    @Dict(categoryCode = "work_experience")
    private Integer workExperience;

    /**
     * 最低薪资
     */
    private Integer minSalary;

    /**
     * 最高薪资
     */
    private Integer maxSalary;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 是否存在推荐奖励
     */
    private Integer isAwardExist;
}
