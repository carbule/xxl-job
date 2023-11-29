package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("job_audit_record")
public class JobAuditRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 职位id
     */
    @TableField("job_id")
    private Long jobId;

    /**
     * 操作 1-拒绝 2-同意
     */
    @TableField("operate")
    private Integer operate;

    /**
     * 拒绝理由
     */
    @TableField("refuse_reason")
    private String refuseReason;

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