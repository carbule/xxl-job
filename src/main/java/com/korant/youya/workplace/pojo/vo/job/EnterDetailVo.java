package com.korant.youya.workplace.pojo.vo.job;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

/**
 * @ClassName EnterDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/5 14:04
 * @Version 1.0
 */
@Data
public class EnterDetailVo {

    /**
     * 企业id
     */
    private Long id;

    /**
     * 企业名称
     */
    private String name;

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
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 联系地址
     */
    private String contactAddress;
}
