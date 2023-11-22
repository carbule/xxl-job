package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 企业代办事项表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("enterprise_todo")
public class EnterpriseTodo implements Serializable {

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
     * 用户id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 工号
     */
    @TableField("employee_id")
    private String employeeId;

    /**
     * 事项类型 1-hr认证 2-员工认证
     */
    @TableField("event_type")
    private Integer eventType;

    /**
     * 操作 0-待审核 1-拒绝 2-同意
     */
    @TableField("operate")
    private Integer operate;

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
