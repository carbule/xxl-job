package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 友涯钱包账户提现记录表
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-02-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("yy_wallet_withdrawal_record")
public class WalletWithdrawalRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 钱包账户id
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 提现金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 货币代码
     */
    @TableField("currency")
    private String currency;

    /**
     * 提现方式
     */
    @TableField("withdrawal_method")
    private Integer withdrawalMethod;

    /**
     * 收款账户
     */
    @TableField("payment_account")
    private String paymentAccount;

    /**
     * 提现手续费
     */
    @TableField("fee_amount")
    private BigDecimal feeAmount;

    /**
     * 提现状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 拒绝原因
     */
    @TableField("rejection_reason")
    private String rejectionReason;

    /**
     * 提现请求发起时间
     */
    @TableField("request_time")
    private LocalDateTime requestTime;

    /**
     * 处理开始时间
     */
    @TableField("processing_time")
    private LocalDateTime processingTime;

    /**
     * 提现完成时间
     */
    @TableField("completion_time")
    private LocalDateTime completionTime;

    /**
     * 提现相关备注
     */
    @TableField("remark")
    private String remark;

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
