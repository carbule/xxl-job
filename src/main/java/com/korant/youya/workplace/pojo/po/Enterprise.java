package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 企业信息表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("enterprise")
public class Enterprise implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企业id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业名称
     */
    @TableField("name")
    private String name;

    /**
     * 企业logo
     */
    @TableField("logo")
    private String logo;

    /**
     * 社会信用代码
     */
    @TableField("social_credit_code")
    private String socialCreditCode;

    /**
     * 成立日期
     */
    @TableField("establish_date")
    private LocalDate establishDate;

    /**
     * 法人
     */
    @TableField("corporation")
    private String corporation;

    /**
     * 注册地址
     */
    @TableField("register_address")
    private String registerAddress;

    /**
     * 企业类型
     */
    @TableField("ent_type")
    private Integer entType;

    /**
     * 企业规模
     */
    @TableField("scale")
    private Integer scale;

    /**
     * 融资阶段
     */
    @TableField("financing_stage")
    private Integer financingStage;

    /**
     * 企业网址
     */
    @TableField("website")
    private String website;

    /**
     * 企业简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 授权书
     */
    @TableField("power_of_attorney")
    private String powerOfAttorney;

    /**
     * 营业执照
     */
    @TableField("business_license")
    private String businessLicense;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 国家编码
     */
    @TableField("country_code")
    private String countryCode;

    /**
     * 省份编码
     */
    @TableField("province_code")
    private String provinceCode;

    /**
     * 市级编码
     */
    @TableField("city_code")
    private String cityCode;

    /**
     * 联系地址
     */
    @TableField("contact_address")
    private String contactAddress;

    /**
     * 认证状态
     */
    @TableField("auth_status")
    private Integer authStatus;

    /**
     * 认证时间
     */
    @TableField("certification_time")
    private LocalDateTime certificationTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    private Integer isDelete;
}
