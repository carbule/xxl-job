package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 候选人信息表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("candidate")
public class Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * HRid
     */
    @TableField("hr")
    private Long hr;

    /**
     * 候选人id
     */
    @TableField("candidate")
    private Long candidate;

    /**
     * 推荐人id
     */
    @TableField("referee")
    private Long referee;

    /**
     * 职位id
     */
    @TableField("job_id")
    private Long jobId;

    /**
     * 申请流程环节 0-初始环节 1-已面试 2-已入职
     */
    @TableField("process_link")
    private Integer processLink;

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