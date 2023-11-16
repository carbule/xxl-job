package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 友涯用户表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("yy_user")
public class User implements Serializable {

    @Serial
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
    private LocalDateTime birthday;

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
     * 名字公开状态 0-否 1-是
     */
    @TableField("name_public_status")
    private Integer namePublicStatus;

    /**
     * 手机号公开状态 0-否 1-是
     */
    @TableField("phone_public_status")
    private Integer phonePublicStatus;

    /**
     * 微信号公开状态 0-否 1-是
     */
    @TableField("wechat_public_status")
    private Integer wechatPublicStatus;

    /**
     * QQ公开状态 0-否 1-是
     */
    @TableField("qq_public_status")
    private Integer qqPublicStatus;

    /**
     * 邮箱公开状态 0-否 1-是
     */
    @TableField("email_public_status")
    private Integer emailPublicStatus;

    /**
     * 地址公开状态 0-否 1-是
     */
    @TableField("address_public_status")
    private Integer addressPublicStatus;

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
     * 国家id
     */
    @TableField("country_id")
    private Long countryId;

    /**
     * 省份id
     */
    @TableField("province_id")
    private Long provinceId;

    /**
     * 市级id
     */
    @TableField("city_id")
    private Long cityId;

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
