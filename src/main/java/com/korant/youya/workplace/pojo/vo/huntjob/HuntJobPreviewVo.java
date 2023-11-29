package com.korant.youya.workplace.pojo.vo.huntjob;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

import java.util.List;

/**
 * @ClassName HuntJobPreviewVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/27 15:24
 * @Version 1.0
 */
@Data
public class HuntJobPreviewVo {

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
     * 年龄
     */
    private Integer age;

    /**
     * 学校名称
     */
    private String schoolName;

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
     * 工作履历
     */
    private List<WorkExperienceVo> workExperienceVoList;
}