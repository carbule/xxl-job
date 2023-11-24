package com.korant.youya.workplace.pojo.vo.enterprise;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

/**
 * @Date 2023/11/20 13:56
 * @PackageName:com.korant.youya.workplace.pojo.vo.enterprise
 * @ClassName: EnterpriseDetailVo
 * @Description:
 * @Version 1.0
 */
@Data
public class EnterpriseDetailVo {

    /**
     * 企业id
     */
    private Long id;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 社会信用代码
     */
    private String socialCreditCode;

    /**
     * 注册地址
     */
    private String registerAddress;

    /**
     * 企业logo
     */
    private String logo;

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
     * 营业执照
     */
    private String businessLicense;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 认证状态
     */
    private Integer authStatus;

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
     * 行政区名称
     */
    private String districtName;

    /**
     * 联系地址
     */
    private String contactAddress;

}
