package com.korant.youya.workplace.pojo.vo.candidate;

import com.korant.youya.workplace.annotations.Dict;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperiencePreviewVo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName CandidateDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/4 14:38
 * @Version 1.0
 */
@Data
public class CandidateDetailVo {

    /**
     * 申请id
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
     * 求职状态
     */
    @Dict(categoryCode = "employ_status")
    private Integer employStatus;

    /**
     * 期望行业
     */
    private String expectedIndustry;

    /**
     * 期望领域
     */
    private String expectedSector;

    /**
     * 期望职位
     */
    private String expectedPosition;

    /**
     * 期望国家
     */
    private String expectedCountry;

    /**
     * 期望省市
     */
    private String expectedProvince;

    /**
     * 期望市级
     */
    private String expectedCity;

    /**
     * 最低薪资
     */
    private Integer minSalary;

    /**
     * 最高薪资
     */
    private Integer maxSalary;

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
     * 详细地址
     */
    private String address;

    /**
     * 工作履历
     */
    private List<WorkExperiencePreviewVo> workExperienceVoList;
}
