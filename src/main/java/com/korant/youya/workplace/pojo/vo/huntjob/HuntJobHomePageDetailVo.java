package com.korant.youya.workplace.pojo.vo.huntjob;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

import java.util.List;

/**
 * @ClassName HuntJobHomePageDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/24 16:35
 * @Version 1.0
 */
@Data
public class HuntJobHomePageDetailVo {

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
     * 用户性别
     */
    private Integer gender;

    /**
     * 个性签名
     */
    private String personalSignature;

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
     * 期望最低工资
     */
    private Integer minExpectedSalary;

    /**
     * 期望最大工资
     */
    private Integer maxExpectedSalary;

    /**
     * 奖励金额
     */
    private Integer award;

    /**
     * 求职状态
     */
    @Dict(categoryCode = "employ_status")
    private Integer employStatus;

    /**
     * 求职说明
     */
    private String description;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户微信号
     */
    private String wechatId;

    /**
     * 用户QQ号
     */
    private String qq;

    /**
     * 用户邮箱
     */
    private String email;

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
     * 行政区名称
     */
    private String districtName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 是否关注求职
     */
    private Integer isCollected;

    /**
     * 工作履历
     */
    private List<WorkExperienceVo> workExperienceVoList;
}
