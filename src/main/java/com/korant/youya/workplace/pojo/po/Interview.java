package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 面试记录表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("interview")
public class Interview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 招聘流程实例id
     */
    @TableField("recruit_process_instance_id")
    private Long recruitProcessInstanceId;

    /**
     * 面试方式
     */
    @TableField("approach")
    private Integer approach;

    /**
     * 面试时间
     */
    @TableField("inter_time")
    private LocalDateTime interTime;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 接受状态 0-待接受 1-拒绝 2-接受
     */
    @TableField("acceptance_status")
    private Integer acceptanceStatus;

    /**
     * 完成状态 0-未完成 1-已取消 2-已完成
     */
    @TableField("completion_status")
    private Integer completionStatus;

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
    @TableLogic
    private Integer isDelete;
}
