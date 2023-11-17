package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户意向职位表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("expected_position")
public class ExpectedPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 状态id
     */
    @TableField("status_id")
    private Long statusId;

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
