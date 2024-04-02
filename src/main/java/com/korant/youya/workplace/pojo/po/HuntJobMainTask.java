package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 企业招聘任务主表
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-03-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("hunt_job_main_task")
public class HuntJobMainTask implements Serializable {

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
     * 内推id
     */
    @TableField("inter_id")
    private Long interId;

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
     * 是否删除 0-未删除 1-已删除
     */
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    private Integer isDelete;
}
