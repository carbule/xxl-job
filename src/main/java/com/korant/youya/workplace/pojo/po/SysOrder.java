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
 * 系统订单表
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-02-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("yy_sys_order")
public class SysOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品id
     */
    @TableField("sys_product_id")
    private Long sysProductId;

    /**
     * 购买方id
     */
    @TableField("buyer_id")
    private Long buyerId;

    /**
     * 购买方姓名
     */
    @TableField("buyer_name")
    private String buyerName;

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
     * 订单类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 支付方式
     */
    @TableField("payment_method")
    private Integer paymentMethod;

    /**
     * 下单日期
     */
    @TableField("order_date")
    private LocalDateTime orderDate;

    /**
     * 购买数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 总金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 平台抽成金额
     */
    @TableField("commission_amount")
    private BigDecimal commissionAmount;

    /**
     * 优惠金额
     */
    @TableField("discount_amount")
    private BigDecimal discountAmount;

    /**
     * 实付金额
     */
    @TableField("actual_amount")
    private BigDecimal actualAmount;

    /**
     * 货币代码
     */
    @TableField("currency")
    private String currency;

    /**
     * 支付平台唯一交易流水号
     */
    @TableField("out_transaction_id")
    private String outTransactionId;

    /**
     * 订单状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 发票id
     */
    @TableField("invoice_id")
    private Long invoiceId;

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
