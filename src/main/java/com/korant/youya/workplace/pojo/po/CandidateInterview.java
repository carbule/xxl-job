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
 * 候选人面试记录表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("candidate_interview")
public class CandidateInterview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 候选记录id
     */
    @TableField("candidate_id")
    private Long candidateId;

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
    private Integer isDelete;
}
