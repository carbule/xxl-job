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
 * 友涯抽成记录表
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-02-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("yy_sys_commission_record")
public class SysCommissionRecord implements Serializable {

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
     * 职位id
     */
    @TableField("job_id")
    private Long jobId;

    /**
     * 求职id
     */
    @TableField("hunt_id")
    private Long huntId;

    /**
     * 抽成类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 流程类型
     */
    @TableField("process_type")
    private Integer processType;

    /**
     * 总金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 抽成比例
     */
    @TableField("commission_rate")
    private BigDecimal commissionRate;

    /**
     * 抽成金额
     */
    @TableField("commission_amount")
    private BigDecimal commissionAmount;

    /**
     * 抽成状态
     */
    @TableField("status")
    private Integer status;

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
