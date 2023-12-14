package com.korant.youya.workplace.pojo.vo.enterprise;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

import java.util.List;

/**
 * @ClassName EnterpriseStructureInfoVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/13 17:36
 * @Version 1.0
 */
@Data
public class EnterpriseStructureInfoVo {

    /**
     * 企业id
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
     * 行政区名称
     */
    private String districtName;

    /**
     * 联系地址
     */
    private String contactAddress;

    /**
     * hr总人数
     */
    private Integer hrNumber;

    /**
     * 员工总人数
     */
    private Integer employeeNumber;

    /**
     * hr头像
     */
    private List<String> hrAvatarList;

    /**
     * 员工头像
     */
    private List<String> employeeAvatarList;
}
