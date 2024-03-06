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
 * 友涯用户表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("yy_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 用户姓氏
     */
    @TableField("last_name")
    private String lastName;

    /**
     * 用户名字
     */
    @TableField("first_name")
    private String firstName;

    /**
     * 用户身份证号
     */
    @TableField("identity_card")
    private String identityCard;

    /**
     * 用户密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户性别
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 用户生日
     */
    @TableField("birthday")
    private LocalDate birthday;

    /**
     * 用户开始工作时间
     */
    @TableField("start_working_time")
    private String startWorkingTime;

    /**
     * 用户手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户微信号
     */
    @TableField("wechat_id")
    private String wechatId;

    /**
     * 用户QQ号
     */
    @TableField("qq")
    private String qq;

    /**
     * 用户邮箱
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
     * 详细地址
     */
    @TableField("address")
    private String address;

    /**
     * 政治面貌
     */
    @TableField("political_outlook")
    private Integer politicalOutlook;

    /**
     * 个性签名
     */
    @TableField("personal_signature")
    private String personalSignature;

    /**
     * 自我评价
     */
    @TableField("self_evaluation")
    private String selfEvaluation;

    /**
     * 实名认证状态
     */
    @TableField("authentication_status")
    private Integer authenticationStatus;

    /**
     * 账号状态 0-未冻结 1-已冻结
     */
    @TableField("account_status")
    private Integer accountStatus;

    /**
     * 微信友涯商户下用户openid
     */
    @TableField("wechat_open_id")
    private String wechatOpenId;

    /**
     * 支付宝友涯商户下用户openid
     */
    @TableField("alipay_open_id")
    private String alipayOpenId;

    /**
     * 用户支付宝账号
     */
    @TableField("alipay_account")
    private String alipayAccount;

    /**
     * 用户支付宝账号姓名
     */
    @TableField("alipay_account_name")
    private String alipayAccountName;

    /**
     * 用户支付宝账号身份证号
     */
    @TableField("alipay_account_identity_card")
    private String alipayAccountIdentityCard;

    /**
     * 预留字段1
     */
    @TableField("reserved_field_1")
    private String reservedField1;

    /**
     * 预留字段2
     */
    @TableField("reserved_field_2")
    private String reservedField2;

    /**
     * 预留字段3
     */
    @TableField("reserved_field_3")
    private String reservedField3;

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
