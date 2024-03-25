package com.korant.youya.workplace.pojo.vo.huntjob;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName HuntJobHomePageVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/8 17:20
 * @Version 1.0
 */
@Data
public class HuntJobHomePageVo {

    /**
     * 求职id
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
     * 职位
     */
    private String positionName;

    /**
     * 期望最低工资
     */
    private Integer minExpectedSalary;

    /**
     * 期望最大工资
     */
    private Integer maxExpectedSalary;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 入职成功奖金
     */
    private BigDecimal onboardingAward;
}
