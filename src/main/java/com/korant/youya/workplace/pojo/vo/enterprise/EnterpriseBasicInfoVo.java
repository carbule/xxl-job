package com.korant.youya.workplace.pojo.vo.enterprise;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

/**
 * @ClassName EnterpriseBasicInfoVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/13 17:33
 * @Version 1.0
 */
@Data
public class EnterpriseBasicInfoVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 企业类型
     */
    @Dict(categoryCode = "enterprise_type")
    private Integer entType;

    /**
     * 企业规模
     */
    @Dict(categoryCode = "enterprise_scale")
    private Integer scale;

    /**
     * 融资阶段
     */
    @Dict(categoryCode = "financing_stage")
    private Integer financingStage;

    /**
     * 企业网址
     */
    private String website;

    /**
     * 企业简介
     */
    private String introduction;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
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
     * 联系地址
     */
    private String contactAddress;
}
