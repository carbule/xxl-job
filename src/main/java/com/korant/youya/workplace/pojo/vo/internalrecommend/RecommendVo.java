package com.korant.youya.workplace.pojo.vo.internalrecommend;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

/**
 * @ClassName RecommendVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/22 17:53
 * @Version 1.0
 */
@Data
public class RecommendVo {

    /**
     * 主键
     */
    private Long id;

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
     * 个性签名
     */
    private String personalSignature;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 学历要求
     */
    @Dict(categoryCode = "education")
    private Integer eduLevel;

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
     * 推荐奖励
     */
    private Integer award;

    /**
     * 关联职位名称
     */
    private String associationPositionName;

    /**
     * 企业logo
     */
    private String logo;

    /**
     * 企业名称
     */
    private String enterpriseName;
}
