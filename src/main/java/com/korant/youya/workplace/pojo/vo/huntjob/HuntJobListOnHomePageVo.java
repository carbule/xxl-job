package com.korant.youya.workplace.pojo.vo.huntjob;

import lombok.Data;

/**
 * @ClassName HuntJobListVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/8 17:20
 * @Version 1.0
 */
@Data
public class HuntJobListOnHomePageVo {

    /**
     * 求职id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

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
     * 工作类型 1-全职 2-兼职
     */
    private Integer jobType;

    /**
     * 期望最低工资
     */
    private Integer minExpectedSalary;

    /**
     * 期望最大工资
     */
    private Integer maxExpectedSalary;

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 职位
     */
    private String position;

    /**
     * 奖励金额
     */
    private Integer award;

    /**
     * 发布天数
     */
    private Integer publishDays;
}
