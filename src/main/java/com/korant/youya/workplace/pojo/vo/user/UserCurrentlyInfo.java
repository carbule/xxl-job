package com.korant.youya.workplace.pojo.vo.user;

import lombok.Data;

/**
 * @Date 2023/11/24 14:18
 * @PackageName:com.korant.youya.workplace.pojo.vo.user
 * @ClassName: UserCurrentlyInfo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class UserCurrentlyInfo {

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
     * 实名认证状态
     */
    private Integer authenticationStatus;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 角色类型
     */
    private String role;

}
