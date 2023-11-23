package com.korant.youya.workplace.pojo.vo.userprivacy;

import lombok.Data;

/**
 * @ClassName UserPersonalInfoPrivacyVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/22 17:47
 * @Version 1.0
 */
@Data
public class UserPersonalInfoPrivacyVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 名字公开状态值 1-显示全名 2-只显示姓 3-只显示名
     */
    private Integer namePublicStatusValue;

    /**
     * 名字公开状态名称
     */
    private String namePublicStatusName;

    /**
     * 手机号公开状态值 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    private Integer phonePublicStatusValue;

    /**
     * 手机号公开状态名称
     */
    private String phonePublicStatusName;

    /**
     * 微信号公开状态值 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    private Integer wechatPublicStatusValue;

    /**
     * 微信号公开状态名称
     */
    private String wechatPublicStatusName;

    /**
     * QQ公开状态值 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    private Integer qqPublicStatusValue;

    /**
     * QQ公开状态名称
     */
    private String qqPublicStatusName;

    /**
     * 邮箱公开状态值 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    private Integer emailPublicStatusValue;

    /**
     * 邮箱公开状态名称
     */
    private String emailPublicStatusName;

    /**
     * 地址公开状态 1-所有人可见 2-只对招聘方可见 3-只对自己可见
     */
    private Integer addressPublicStatusValue;

    /**
     * 地址公开状态名称
     */
    private String addressPublicStatusName;
}
