package com.korant.youya.workplace.pojo.vo.user;

import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/21 15:57
 * @PackageName:com.korant.youya.workplace.pojo.vo.user
 * @ClassName: ResumeContactInfoVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class ResumeContactInfoVo {

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
