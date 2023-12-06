package com.korant.youya.workplace.pojo.vo.user;

import lombok.Data;

/**
 * @ClassName LoginUserVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/6 16:21
 * @Version 1.0
 */
@Data
public class LoginUserVo {

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户姓氏
     */
    private String lastName;

    /**
     * 用户名字
     */
    private String firstName;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 实名认证状态
     */
    private Integer authenticationStatus;

    /**
     * 角色
     */
    private String role;
}
