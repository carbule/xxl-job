package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 职位申请表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("apply_job")
public class ApplyJob implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 申请人id
     */
    @TableField("applicant")
    private Long applicant;

    /**
     * 职位id
     */
    @TableField("job_id")
    private Long jobId;

    /**
     * 分享人id
     */
    @TableField("referee")
    private Long referee;

    /**
     * 招聘流程实列id
     */
    @TableField("recruit_process_instance_id")
    private Long recruitProcessInstanceId;

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
