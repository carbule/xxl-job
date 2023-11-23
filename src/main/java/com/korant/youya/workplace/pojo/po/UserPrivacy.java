package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户个人信息隐私设置
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("yy_user_privacy")
public class UserPrivacy implements Serializable {

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
     * 名字公开状态 1-显示全名 2-只显示姓 3-只显示名
     */
    @TableField("name_public_status")
    private Integer namePublicStatus;

    /**
     * 手机号公开状态 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    @TableField("phone_public_status")
    private Integer phonePublicStatus;

    /**
     * 微信号公开状态 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    @TableField("wechat_public_status")
    private Integer wechatPublicStatus;

    /**
     * QQ公开状态 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    @TableField("qq_public_status")
    private Integer qqPublicStatus;

    /**
     * 邮箱公开状态 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    @TableField("email_public_status")
    private Integer emailPublicStatus;

    /**
     * 地址公开状态 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    @TableField("address_public_status")
    private Integer addressPublicStatus;

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
