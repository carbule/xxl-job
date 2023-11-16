package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户意向职位表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("Intended_position")
public class IntendedPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 意向id
     */
    @TableField("intention_id")
    private Long intentionId;

    /**
     * 行业id
     */
    @TableField("industry_id")
    private Long industryId;

    /**
     * 领域id
     */
    @TableField("sector_id")
    private Long sectorId;

    /**
     * 职位id
     */
    @TableField("position_id")
    private Long positionId;

    /**
     * 最低薪资
     */
    @TableField("min_salary")
    private Integer minSalary;

    /**
     * 最高薪资
     */
    @TableField("max_salary")
    private Integer maxSalary;

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
