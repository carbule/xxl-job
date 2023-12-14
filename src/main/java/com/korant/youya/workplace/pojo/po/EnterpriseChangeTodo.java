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
 * 企业变更操作待办表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("enterprise_change_todo")
public class EnterpriseChangeTodo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业id
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 企业名称
     */
    @TableField("name")
    private String name;

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
     * 变更类型 1-名称变成 2-地址变更
     */
    @TableField("type")
    private Integer type;

    /**
     * 操作 0-待审批 1-拒绝 2-同意
     */
    @TableField("operate")
    private Integer operate;

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
