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
 * 求职表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("hunt_job")
public class HuntJob implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 意向职位id
     */
    @TableField("position_id")
    private Long positionId;

    /**
     * 期望区域id
     */
    @TableField("area_id")
    private Long areaId;

    /**
     * 推荐奖励
     */
    @TableField("award")
    private Integer award;

    /**
     * 面试奖励分配比例
     */
    @TableField("interview_reward_rate")
    private Integer interviewRewardRate;

    /**
     * 入职奖励分配比例
     */
    @TableField("onboard_reward_rate")
    private Integer onboardRewardRate;

    /**
     * 转正奖励分配比例
     */
    @TableField("full_member_reward_rate")
    private Integer fullMemberRewardRate;

    /**
     * 求职状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 刷新时间
     */
    @TableField("refresh_time")
    private LocalDateTime refreshTime;

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