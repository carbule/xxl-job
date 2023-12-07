package com.korant.youya.workplace.pojo.vo.attentionhuntjob;

import lombok.Data;

/**
 * @ClassName AttentionHuntJobPersonalVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/5 17:13
 * @Version 1.0
 */
@Data
public class AttentionHuntJobPersonalVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 求职id
     */
    private Long huntJobId;

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
     * 行政区名称
     */
    private String districtName;

    /**
     * 奖励金额
     */
    private Integer award;
}
