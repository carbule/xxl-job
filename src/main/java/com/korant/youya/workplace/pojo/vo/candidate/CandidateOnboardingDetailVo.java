package com.korant.youya.workplace.pojo.vo.candidate;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName CandidateOnboardingDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/11 11:25
 * @Version 1.0
 */
@Data
public class CandidateOnboardingDetailVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 招聘流程实列id
     */
    private Long recruitProcessInstanceId;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户姓氏
     */
    private String lastName;

    /**
     * 用户名
     */
    private String firstName;

    /**
     * 用户性别
     */
    private Integer gender;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 职位名称
     */
    private String positionName;

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
    private String workAreaCityName;

    /**
     * 关联职位名称
     */
    private String associationPositionName;

    /**
     * 关联职位最低薪资
     */
    private Integer associationPositionMinSalary;

    /**
     * 关联职位最高薪资
     */
    private Integer associationPositionMaxSalary;

    /**
     * 入职时间
     */
    private LocalDateTime onboardingTime;

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 入职地点
     */
    private String address;

    /**
     * 备注
     */
    private String note;

    /**
     * 接受状态 0-待接受 1-拒绝 2-接受
     */
    private Integer acceptanceStatus;

    /**
     * 完成状态 0-未完成 1-已取消 2-已完成
     */
    private Integer completionStatus;
}
