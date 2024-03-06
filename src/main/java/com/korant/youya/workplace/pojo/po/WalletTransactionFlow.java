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
 * 友涯钱包账户交易流水表
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-02-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("yy_wallet_transaction_flow")
public class WalletTransactionFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账户id
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 账户姓名
     */
    @TableField("account_name")
    private String accountName;

    /**
     * 商家id
     */
    @TableField("seller_id")
    private Long sellerId;

    /**
     * 商家名称
     */
    @TableField("seller_name")
    private String sellerName;

    /**
     * 商品id
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 交易类型
     */
    @TableField("transaction_type")
    private Integer transactionType;

    /**
     * 交易方向 1-入账 2-出账
     */
    @TableField("transaction_direction")
    private Integer transactionDirection;

    /**
     * 交易金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 货币代码
     */
    @TableField("currency")
    private String currency;

    /**
     * 交易简要描述
     */
    @TableField("description")
    private String description;

    /**
     * 交易发起日期
     */
    @TableField("initiation_date")
    private LocalDateTime initiationDate;

    /**
     * 交易完成日期
     */
    @TableField("completion_date")
    private LocalDateTime completionDate;

    /**
     * 交易状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 交易状态描述
     */
    @TableField("trade_status_desc")
    private String tradeStatusDesc;

    /**
     * 支付平台唯一交易流水号
     */
    @TableField("out_transaction_id")
    private String outTransactionId;

    /**
     * 交易失败原因
     */
    @TableField("transaction_fail_reason")
    private String transactionFailReason;

    /**
     * 交易凭证号
     */
    @TableField("reference_number")
    private Long referenceNumber;

    /**
     * 交易前账户余额
     */
    @TableField("balance_before")
    private BigDecimal balanceBefore;

    /**
     * 交易后账户余额
     */
    @TableField("balance_after")
    private BigDecimal balanceAfter;

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
