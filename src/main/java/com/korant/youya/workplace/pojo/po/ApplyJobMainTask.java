package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 求职任务主表
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-02-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("apply_job_main_task")
public class ApplyJobMainTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 求职id
     */
    @TableField("apply_id")
    private Long applyId;

    /**
     * 流程环节
     */
    @TableField("process_step")
    private Integer processStep;

    /**
     * 奖金分配规则
     */
    @TableField("bonus_distribution_rule")
    private String bonusDistributionRule;

    /**
     * 任务状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 截止日期
     */
    @TableField("deadline")
    private LocalDateTime deadline;

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
     * 任务状态
     */
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    private Integer isDelete;
}
