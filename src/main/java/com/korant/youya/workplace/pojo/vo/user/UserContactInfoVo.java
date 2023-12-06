package com.korant.youya.workplace.pojo.vo.user;

import lombok.Data;

/**
 * @ClassName UserContactInfoVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/6 14:42
 * @Version 1.0
 */
@Data
public class UserContactInfoVo {

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
     * 国家编码
     */
    private String countryCode;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 市级编码
     */
    private String cityCode;

    /**
     * 行政区编码
     */
    private String districtCode;

    /**
     *  详细地址
     */
    private String address;
}
