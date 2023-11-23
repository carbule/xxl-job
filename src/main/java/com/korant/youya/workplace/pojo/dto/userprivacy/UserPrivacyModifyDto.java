package com.korant.youya.workplace.pojo.dto.userprivacy;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName UserPrivacyModifyDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/23 10:09
 * @Version 1.0
 */
@Data
public class UserPrivacyModifyDto {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 名字公开状态 1-显示全名 2-只显示姓 3-只显示名
     */
    @NotNull(message = "名字公开状态不能为空")
    private Integer namePublicStatus;

    /**
     * 手机号公开状态 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    @NotNull(message = "手机号公开状态不能为空")
    private Integer phonePublicStatus;

    /**
     * 微信号公开状态 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    @NotNull(message = "微信号公开状态不能为空")
    private Integer wechatPublicStatus;

    /**
     * QQ公开状态 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    @NotNull(message = "QQ公开状态不能为空")
    private Integer qqPublicStatus;

    /**
     * 邮箱公开状态 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    @NotNull(message = "邮箱公开状态不能为空")
    private Integer emailPublicStatus;

    /**
     * 地址公开状态 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    @NotNull(message = "地址公开状态不能为空")
    private Integer addressPublicStatus;
}
