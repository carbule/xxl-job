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
 * 奖金分配规则
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-03-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("bonus_distribution_rule")
public class BonusDistributionRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 规则名称
     */
    @TableField("name")
    private String name;

    /**
     * 规则编码
     */
    @TableField("code")
    private String code;

    /**
     * 分享者比例
     */
    @TableField("sharer_ratio")
    private BigDecimal sharerRatio;

    /**
     * 推荐者比例
     */
    @TableField("recommender_ratio")
    private BigDecimal recommenderRatio;

    /**
     * 平台抽成比例
     */
    @TableField("platform_ratio")
    private BigDecimal platformRatio;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

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
