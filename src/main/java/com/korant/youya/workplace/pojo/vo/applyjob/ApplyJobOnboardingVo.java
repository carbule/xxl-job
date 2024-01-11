package com.korant.youya.workplace.pojo.vo.applyjob;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName ApplyJobOnboardingVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/11 15:50
 * @Version 1.0
 */
@Data
public class ApplyJobOnboardingVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 入职时间
     */
    private LocalDateTime onboardingTime;

    /**
     * 国家编码
     */
    private String countryName;

    /**
     * 省份编码
     */
    private String provinceName;

    /**
     * 市级编码
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

    /**
     * hr头像
     */
    private String avatar;

    /**
     * hr姓氏
     */
    private String lastName;

    /**
     * hr名称
     */
    private String firstName;
}