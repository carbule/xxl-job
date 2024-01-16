package com.korant.youya.workplace.pojo.vo.huntjob;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

/**
 * @ClassName HuntJobShareInfo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/12 10:25
 * @Version 1.0
 */
@Data
public class HuntJobShareInfo {

    /**
     * 求职id
     */
    private Long id;

    /**
     * 申请人头像
     */
    private String applicantAvatar;

    /**
     * 申请人姓氏
     */
    private String applicantLastName;

    /**
     * 申请人名字
     */
    private String applicantFirstName;

    /**
     * 申请人性别
     */
    private Integer applicantGender;

    /**
     * 自我评价
     */
    private String selfEvaluation;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 学历
     */
    @Dict(categoryCode = "education")
    private Integer eduLevel;

    /**
     * 工作年限
     */
    private Integer workExperience;

    /**
     * 奖励金额
     */
    private Integer award;

    /**
     * 推荐人头像
     */
    private String refereeAvatar;

    /**
     * 推荐人姓氏
     */
    private String refereeLastName;

    /**
     * 推荐人性别
     */
    private Integer refereeGender;

    /**
     * 推荐人名字
     */
    private String refereeFirstName;
}
