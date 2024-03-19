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
     * 行业编码
     */
    @TableField("industry_code")
    private String industryCode;

    /**
     * 领域编码
     */
    @TableField("sector_code")
    private String sectorCode;

    /**
     * 职位编码
     */
    @TableField("position_code")
    private String positionCode;

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

    /**
     * 职业类型编码
     */
    private String typeCode;
}
