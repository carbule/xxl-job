package com.korant.youya.workplace.pojo.vo.internalrecommend;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName InternalRecommendVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/2 17:03
 * @Version 1.0
 */
@Data
public class InternalRecommendVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 企业logo
     */
    private String logo;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 企业规模
     */
    @Dict(categoryCode = "enterprise_scale")
    private Integer scale;

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
     * 是否存在推荐奖励
     */
    private Integer isAwardExist;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户姓氏
     */
    private String lastName;

    /**
     * 用户名字
     */
    private String firstName;

    /**
     * 推荐时间
     */
    private LocalDateTime recommendedTime;
}
