package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/20 13:52
 * @ClassName: EnterpriseModifyDto
 * @Description:
 * @Version 1.0
 */
@Data
public class EnterpriseModifyDto {

    /**
     * 企业id
     */
    @NotNull(message = "企业id不能为空")
    private Long id;

    /**
     * 企业类型
     */
    private Integer entType;

    /**
     * 企业规模
     */
    private Integer scale;

    /**
     * 融资阶段
     */
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
    @Email(message = "邮箱格式错误")
    private String email;

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
     * 联系地址
     */
    private String contactAddress;

}
