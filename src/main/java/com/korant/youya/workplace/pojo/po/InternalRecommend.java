package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 内部推荐表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("internal_recommend")
public class InternalRecommend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 求职id
     */
    @TableField("hunt_id")
    private Long huntId;

    /**
     * 职位id
     */
    @TableField("job_id")
    private Long jobId;

    /**
     * 推荐人id
     */
    @TableField("referee")
    private Long referee;

    /**
     * HRid
     */
    @TableField("hr")
    private Long hr;

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
