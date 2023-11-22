package com.korant.youya.workplace.pojo.vo.user;

import lombok.Data;

/**
 * @Date 2023/11/21 13:47
 * @PackageName:com.korant.youya.workplace.pojo.vo.user
 * @ClassName: ResumePersonPreviewVo
 * @Description:
 * @Version 1.0
 */
@Data
public class ResumePersonPreviewVo {

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
     * 用户性别
     */
    private Integer gender;

    /**
     * 用户生日
     */
    private Integer age;

    /**
     * 学历
     */
    private Integer eduLevel;

    /**
     * 工龄
     */
    private Integer seniority;

    /**
     * 政治面貌
     */
    private Integer politicalOutlook;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 自我评价
     */
    private String selfEvaluation;

    /**
     * 实名认证状态
     */
    private Integer authenticationStatus;

    /**
     * 求职状态
     */
    private Integer employStatus;

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
     *  详细地址
     */
    private String address;

}
